package com.doctoraak.doctoraakpatient.ui.incubation

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.IncubationsAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityIncubationBinding
import com.doctoraak.doctoraakpatient.model.Incubation
import com.doctoraak.doctoraakpatient.model.IncubationResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.IncubationItemActivity
import com.doctoraak.doctoraakpatient.ui.PlacePickerDialog
import com.doctoraak.doctoraakpatient.utils.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.search_by_address_without_insurrance.view.*
import kotlinx.android.synthetic.main.search_by_city_and_area.view.*
import kotlinx.android.synthetic.main.search_by_city_and_area_wthout_insurrnace.view.*
import kotlinx.android.synthetic.main.search_by_city_and_area_wthout_insurrnace.view.sp_area
import kotlinx.android.synthetic.main.search_by_city_and_area_wthout_insurrnace.view.sp_city
import kotlinx.android.synthetic.main.search_by_city_and_area_wthout_insurrnace.view.spl_area
import kotlinx.android.synthetic.main.search_by_city_and_area_wthout_insurrnace.view.spl_city

class IncubationActivity : BaseActivity() {
    private lateinit var binding: ActivityIncubationBinding
    private val viewModel by lazy { ViewModelProvider(this).get(IncubationViewModel::class.java) }
    private lateinit var adapter: IncubationsAdapter
    /**  1 : city and area selected
     * , 2 : lat and lng selected */
    private var selectedFilterOption = 0

    private val CITY_ID_KEY = "cityId"
    private val AREA_ID_KEY = "areaId"
    private val LATT_KEY = "lat"
    private val LANG_ID_KEY = "lang"
    private val SEARCH_LAYOUT_VISIBIILTY_KEY = "SEARCH_LAYOUT_VISIBIILTY_KEY"
    private val LIST_LAYOUT_VISIBIILTY_KEY = "LIST_LAYOUT_VISIBIILTY_KEY"
    private val BUTTON_VISIBILTY_KEY = "BUTTON_VISIBILTY_KEY"

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        outState.putInt(CITY_ID_KEY , cityId)
        outState.putInt(AREA_ID_KEY , areaId)
        outState.putInt(SEARCH_LAYOUT_VISIBIILTY_KEY , binding.scrollViewContainerSearch.visibility)
        outState.putInt(LIST_LAYOUT_VISIBIILTY_KEY , binding.rvIncubations.visibility)
        outState.putInt(BUTTON_VISIBILTY_KEY , binding.btnSearch.visibility)

        if (lang != null && latt != null) {
            outState.putDouble(LATT_KEY , latt!!)
            outState.putDouble(LANG_ID_KEY , lang!!)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState != null) {

            cityId = savedInstanceState.getInt(CITY_ID_KEY)
            areaId = savedInstanceState.getInt(AREA_ID_KEY)
            binding.scrollViewContainerSearch.visibility = savedInstanceState.getInt(SEARCH_LAYOUT_VISIBIILTY_KEY)
            binding.rvIncubations.visibility = savedInstanceState.getInt(LIST_LAYOUT_VISIBIILTY_KEY)
            binding.btnSearch.visibility = savedInstanceState.getInt(BUTTON_VISIBILTY_KEY)

            if (savedInstanceState.containsKey(LANG_ID_KEY ) &&
                savedInstanceState.containsKey(LATT_KEY)){
                latt = savedInstanceState.getDouble(LATT_KEY)
                lang = savedInstanceState.getDouble(LANG_ID_KEY)
            }
            if (viewModel.incubations.value != null && viewModel.incubations.value!!.data.size > 0){
                adapter.setData(viewModel.incubations.value!!.data)
            }

            (supportFragmentManager.findFragmentByTag(PlacePickerDialog.TAG)
                    as PlacePickerDialog?)?.let {
                it.listener = object : PlacePickerDialog.onPlacePickerListener {
                    override fun onLocationSelected(address: String, position: LatLng) {
                        onPlacePicker(address, position)
                    }
                }
            }
        }
    }

    private fun onPlacePicker(address: String, position: LatLng) {
        latt = position.latitude
        lang = position.longitude
        binding.searchByAddress.et_address
            .setText( address)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_incubation)

        setupAutoCompleteForAreas();
        setupAutoCompleteForCities()

        setRecyclerviewLayout()

        setListenerToRadioButtons()
        observeData()
        binding.btnSearch.setOnClickListener {

            if (binding.searchByAddress.visibility == View.VISIBLE) {
                if (Utils.checkInternetConnection(this , binding.clIncubation)) {
                    viewModel.getIncubations(latt!!, lang!!)
                }
            } else {
                val isValidateCity = cityId.validateCity(this, binding.searchByCity.spl_city)
                val isValidateArea = areaId.validateArea(this, binding.searchByCity.spl_area)
                if (isValidateCity && isValidateArea) {
                    if (Utils.checkInternetConnection(this , binding.clIncubation)) {
                        viewModel.getIncubations(
                            areaId, cityId
                        )
                    }
                }
            }
        }

        binding.searchByAddress.et_address.setOnClickListener {
            checkIfGPSEnabled()
        }

        autoCompleteTextViewSelectListners()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun setListenerToRadioButtons() {
        val motion  = findViewById<MotionLayout>(R.id.motion_layout)
        binding.tvSearchByAddress.setOnClickListener {
            if (binding.searchByCity.visibility != View.GONE) {
                selectedFilterOption = 2

                applyAnimation(R.anim.fade_out, binding.searchByCity , onAnimaitionFinished = {
                    binding.searchByCity.visibility = View.GONE
                    applyAnimation(R.anim.fade_in, binding.searchByAddress)
                    binding.searchByAddress.visibility = View.VISIBLE
                })

                motion.setTransition(R.id.start , R.id.end)
                motion.transitionToEnd()
            }

        }

        binding.tvSearchByCity.setOnClickListener {
            if (binding.searchByAddress.visibility != View.GONE) {
                selectedFilterOption = 1

                applyAnimation(R.anim.fade_out, binding.searchByAddress , onAnimaitionFinished = {
                    binding.searchByAddress.visibility = View.GONE

                    applyAnimation(R.anim.fade_in, binding.searchByCity)
                    binding.searchByCity.visibility = View.VISIBLE
                })

                motion.setTransition(R.id.start2 , R.id.end2)
                motion.transitionToEnd()
            }
        }
    }

    private fun autoCompleteTextViewSelectListners()
    {
        binding.searchByCity.sp_city.setOnItemClickListener(AdapterView.OnItemClickListener
        { parent, view, position, id ->
            val s = parent.getItemAtPosition(position)
            areaId = -1
            binding.searchByCity.sp_area.removeSelectedItem(0)

            if (s.equals(getString(R.string.select_none))) {
                cityId = -1
            } else {
                cityId = Utils.getCityId(s.toString())
                val newAreaList = Utils.getAreasNamesForCityId(cityId)
                newAreaList.add(0 , getString(R.string.select_none))
                setupAutoCompleteTextView(binding.searchByCity.sp_area , newAreaList)
            }
        })

        binding.searchByCity.sp_area.setOnItemClickListener(AdapterView.OnItemClickListener
        { parent, view, position, id ->
            val s = parent.getItemAtPosition(position)
            if (s.equals(getString(R.string.select_none))) {
                areaId = -1
            }else{
                areaId = Utils.getAreaId(s.toString())
            }
        })
    }

    private fun setupAutoCompleteForCities() {
        val cityList = Utils.getCitiessNames()
        cityList.add(0 , getString(R.string.select_none))

        val adapter = ArrayAdapter(
            this, R.layout.autocompletetextview_item
            , cityList
        )

        binding.searchByCity.sp_city.setThreshold(1)
        binding.searchByCity.sp_city.setAdapter(adapter)
    }

    private fun setupAutoCompleteForAreas() {
        val areaList = Utils.getAreasNames()
        areaList.add(0 , getString(R.string.select_none))

        val adapter = ArrayAdapter(
            this, R.layout.autocompletetextview_item
            , areaList
        )

        binding.searchByCity.sp_area.setThreshold(1)
        binding.searchByCity.sp_area.setAdapter(adapter)

    }

    private fun setRecyclerviewLayout()
    {
        binding.rvIncubations.layoutManager = LinearLayoutManager(this)
        binding.rvIncubations.onStartLoadingAction = {
            if (selectedFilterOption == 1)
            {
                viewModel.getIncubations(latt!!, lang!!)
                true
            }
            else
            {
                viewModel.getIncubations(areaId, cityId)
                true
            }

            false
        }

        adapter = IncubationsAdapter(this)
        binding.rvIncubations.adapter = adapter

        onItemClick()
    }

    private fun onItemClick()
    {
        adapter.setOnItemClickListener(object : IncubationsAdapter.ClickListener {
            override fun onClick(model: Incubation, aView: View) {
                val intent = Intent(this@IncubationActivity, IncubationItemActivity::class.java)
                intent.putExtra(
                    getString(com.doctoraak.doctoraakpatient.R.string.incubation_item_key),
                    Utils.convertObjectToJson(model)
                )
                startActivity(intent)
            }

        })
    }

    private fun observeData()
    {
        viewModel.incubations.observe(this, object : Observer<IncubationResponse> {
            override fun onChanged(t: IncubationResponse?) {
                binding.loading!!.visibility = View.INVISIBLE

                // todo pageNumber = 0 why
                Log.d("saif", "pageNum= ${viewModel.pageNum.value!!}")
                if (t!!.data.size > 0 && !binding.rvIncubations.isVisible)
                {
                    binding.scrollViewContainerSearch.visibility = View.INVISIBLE
                    applyAnimation(R.anim.fade_out, binding.scrollViewContainerSearch)

                    binding.btnSearch.visibility = View.INVISIBLE
                    applyAnimation(R.anim.fade_out, binding.btnSearch)

                    binding.rvIncubations.visibility = View.VISIBLE
                    applyAnimation(R.anim.fade_in, binding.rvIncubations)
                }

                if (t!!.data.size == 0)
                    showSnackbar(binding.clIncubation , t.getMsg())

                adapter.setData(t!!.data)
            }
        })

        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    disableButton(binding.btnSearch)
                    binding.loading!!.visibility = View.VISIBLE
                } else {
                    enableButton(binding.btnSearch)
                    binding.loading!!.visibility = View.INVISIBLE
                }

            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clIncubation , t)
                    enableButton(binding.btnSearch)
                    binding.loading!!.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!! != 0) {
                    showSnackbar(binding.clIncubation , getString(t))
                    enableButton(binding.btnSearch)
                    binding.loading!!.visibility = View.INVISIBLE
                }
            }
        })

    }

    fun checkIfGPSEnabled() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayPromptForEnablingGPS(this)
        }else{
            val pl = PlacePickerDialog.newInstance()
            pl.show(supportFragmentManager, PlacePickerDialog.TAG)

            pl.listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng) {
                    onPlacePicker(address, position)
                }
            }
        }
    }

    fun displayPromptForEnablingGPS(activity: Activity) {

        val builder = AlertDialog.Builder(activity)
        val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        val message = getString(R.string.please_open_gps)

        builder.setMessage(message)
            .setPositiveButton(getString(R.string.ok),
                DialogInterface.OnClickListener { d, id ->
                    activity.startActivity(Intent(action))
                    d.dismiss()
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { d, id -> d.cancel() })
        builder.create().show()
    }


    override fun onBackPressed() {
        if (binding.scrollViewContainerSearch.visibility == View.INVISIBLE) {
            binding.rvIncubations.visibility = View.INVISIBLE
            applyAnimation(R.anim.fade_out, binding.rvIncubations )

            binding.scrollViewContainerSearch.visibility = View.VISIBLE
            applyAnimation(R.anim.fade_in, binding.scrollViewContainerSearch)

            binding.btnSearch.visibility = View.VISIBLE
            applyAnimation(R.anim.fade_in, binding.btnSearch)


        } else
            super.onBackPressed()
    }

}
