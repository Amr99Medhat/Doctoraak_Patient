package com.doctoraak.doctoraakpatient.ui.radiology

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.AutoCompleteTextViewAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityRadiologyBinding
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.PlacePickerDialog
import com.doctoraak.doctoraakpatient.ui.lap.LabListActivity
import com.doctoraak.doctoraakpatient.utils.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.search_by_city_and_area.view.*
import kotlinx.android.synthetic.main.search_by_city_and_area_with_radiology_name.view.*

class RadiologyActivity : BaseActivity() {
    private lateinit var binding: ActivityRadiologyBinding
    private val viewModel by lazy { ViewModelProvider(this).get(RadiologyViewModel::class.java) }
    private lateinit var filter: RadiologyFilter

    private val NAME_ID_KEY = "specializtionId"
    private val CITY_ID_KEY = "cityId"
    private val AREA_ID_KEY = "areaId"
    private val LATT_KEY = "lat"
    private val LANG_ID_KEY = "lang"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NAME_ID_KEY, nameId)
        outState.putInt(CITY_ID_KEY, cityId)
        outState.putInt(AREA_ID_KEY, areaId)

        if (lang != null && latt != null) {
            outState.putDouble(LATT_KEY, latt!!)
            outState.putDouble(LANG_ID_KEY, lang!!)
        }
    }

    fun getState(savedInstanceState: Bundle) {
        nameId = savedInstanceState.getInt(NAME_ID_KEY)
        cityId = savedInstanceState.getInt(CITY_ID_KEY)
        areaId = savedInstanceState.getInt(AREA_ID_KEY)
        if (savedInstanceState.containsKey(LANG_ID_KEY) &&
            savedInstanceState.containsKey(LATT_KEY)
        ) {
            latt = savedInstanceState.getDouble(LATT_KEY)
            lang = savedInstanceState.getDouble(LANG_ID_KEY)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState!!)
        (supportFragmentManager.findFragmentByTag(PlacePickerDialog.TAG)
                as PlacePickerDialog?)?.let {
            it.listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng) {
                    onPlacePicker(address, position)
                }
            }
        }
    }

    private fun onPlacePicker(address: String, position: LatLng) {
        latt = position.latitude
        lang = position.longitude
        binding.searchByAddress.etAddress.setText(address)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_radiology)

        if (savedInstanceState != null) {
            getState(savedInstanceState)
        }

        binding.searchByCity.splLabName.hint = getString(R.string.radiology_name)

        val cityList = Utils.getCitiessNames()
        val areaList = Utils.getAreasNames()

        cityList.add(0, getString(R.string.select_none))
        areaList.add(0, getString(R.string.select_none))

        //binding.searchByCity.sp_lab_name it is radiology autocompletetextview
        setupAutoCompleteTextView(binding.searchByCity.spCity, cityList)
        setupAutoCompleteTextView(binding.searchByCity.spArea, areaList)

        if (!SessionManager.isLogIn())
            binding.searchByCity.cbInsurance.hide()

        handleSearchWithNameCityAddressIndicator(binding.searchIndicator,
            binding.searchByCity,
            binding.searchByAddress)
        setListenerToButonAddress()
        setListenerToSearchButton()

        observeData()
        setupListenerToAutoCompleteTextViews()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListenerToAutoCompleteTextViews() {
        binding.searchByCity.spCity.setOnItemClickListener(
            AdapterView.OnItemClickListener
            { parent, view, position, id ->
                val s = parent.getItemAtPosition(position) as String
                areaId = -1
                binding.searchByCity.spArea.removeSelectedItem(0)

                if (s.equals(getString(R.string.select_none))) {
                    cityId = -1
                } else {
                    cityId = Utils.getCityId(s)
                    val newAreaList = Utils.getAreasNamesForCityId(cityId)
                    newAreaList.add(0, getString(com.doctoraak.doctoraakpatient.R.string.select_none))
                    setupAutoCompleteTextView(binding.searchByCity.spArea, newAreaList)
                }
            })

        binding.searchByCity.spArea.setOnItemClickListener(
            AdapterView.OnItemClickListener
            { parent, view, position, id ->
                val s = parent.getItemAtPosition(position) as String
                if (s.equals(getString(R.string.select_none))) {
                    areaId = -1
                } else {
                    areaId = Utils.getAreaId(s)
                }
            })
    }

    private fun setListenerToSearchButton() {
        binding.btnSearch.setOnClickListener {
            viewModel.initPageNumber()

            if (binding.searchByAddress.root.visibility == View.VISIBLE) {
                if (latt.validateLattLang(this, lang, binding.searchByAddress.etlAddress)) {
                    val b: Boolean = binding.searchByAddress.cbInsurance.isChecked
                    val b2: Boolean = binding.searchByAddress.cbDelivery.isChecked
                    if (Utils.checkInternetConnection(this, binding.clRadiology)) {
                        filter = RadiologyFilter(api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else "",
                                patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1,
                                lang = lang!!,
                                latt = latt!!,
                                insurance = b.toInt(),
                                delivery = b2.toInt())
                        getFilteredRadiology()
                    }
                } else {
                    showSnackbar(binding.clRadiology,
                        getString(com.doctoraak.doctoraakpatient.R.string.please_select_location))
                }
            } else {
                val b: Boolean = binding.searchByCity.cbInsurance.isChecked
                val b2: Boolean = binding.searchByCity.cbDelivery.isChecked

                if (binding.searchByCity.splLabName.visibility == View.VISIBLE) {
                    val name = binding.searchByCity.spLabName.text.toString().trim()
                    if (Utils.checkInternetConnection(this , binding.clRadiology)) {
                        if (name.isEmpty())
                        {
                            binding.searchByCity.splLabName.error = getString(R.string.enter_your_name)
                            return@setOnClickListener
                        }
                        else
                            binding.searchByCity.splLabName.error = null


                        filter = RadiologyFilter(api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else "",
                                patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1,
                                name = name,
                                insurance = b.toInt(),
                                delivery = b2.toInt())
                        getFilteredRadiology()
                    }
                } else {
                    val isValidateCity = cityId.validateCity(this, binding.searchByCity.splCity)
                    val isValidateArea = areaId.validateArea(this, binding.searchByCity.splArea)
                    if (isValidateArea && isValidateCity) {
                        if (Utils.checkInternetConnection(this, binding.clRadiology)) {
                            filter =
                                RadiologyFilter(api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else "",
                                    patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1,
                                    city_id = cityId,
                                    area_id = areaId,
                                    insurance = b.toInt(),
                                    delivery = b2.toInt())
                            getFilteredRadiology()
                        }
                    }
                }
            }
        }
    }

    private fun getFilteredRadiology() {
        viewModel.filterRadiologies(filter)
    }

    private fun setListenerToButonAddress() {
        binding.searchByAddress.etAddress.setOnClickListener {
            checkIfGPSEnabled()
        }
    }

    private fun observeData() {
        viewModel.radiologyReponse.observe(this, object : Observer<RadiologyResponse> {
            override fun onChanged(t: RadiologyResponse?) {

                binding.loading!!.visibility = View.GONE
                enableButton(binding.btnSearch)

                if (t!!.data.size > 0) {
                    SessionManager.cacheList(t.data)
                    startActivity(Intent(this@RadiologyActivity,
                        RadiologyListActivity::class.java).apply {
                        putExtra(RadiologyListActivity.FILTER_RADIOLOGY_KEY,
                            Utils.convertObjectToJson(filter))
                    })
                } else
                    toast(t!!.getMsg())
            }
        })
        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {

                if (t!!) {
                    binding.loading!!.visibility = View.VISIBLE
                    disableButton(binding.btnSearch)
                } else {
                    binding.loading!!.visibility = View.GONE
                    enableButton(binding.btnSearch)
                }

            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clRadiology, t)
                    binding.loading!!.visibility = View.GONE
                    enableButton(binding.btnSearch)
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!! != 0) {
                    showSnackbar(binding.clRadiology, getString(t))
                    binding.loading!!.visibility = View.GONE
                    enableButton(binding.btnSearch)
                }
            }
        })

    }

    fun checkIfGPSEnabled() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayPromptForEnablingGPS(this)
        } else {
            val pl = PlacePickerDialog.newInstance();
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
        val message = getString(com.doctoraak.doctoraakpatient.R.string.please_open_gps)

        builder.setMessage(message)
            .setPositiveButton(getString(com.doctoraak.doctoraakpatient.R.string.ok),
                DialogInterface.OnClickListener { d, id ->
                    activity.startActivity(Intent(action))
                    d.dismiss()
                })
            .setNegativeButton(getString(com.doctoraak.doctoraakpatient.R.string.cancel),
                DialogInterface.OnClickListener { d, id -> d.cancel() })
        builder.create().show()
    }

}
