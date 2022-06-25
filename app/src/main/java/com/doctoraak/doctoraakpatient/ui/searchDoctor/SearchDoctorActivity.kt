package com.doctoraak.doctoraakpatient.ui.searchDoctor

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
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.AvailableClinicsAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivitySearchDoctorBinding
import com.doctoraak.doctoraakpatient.model.Clinic
import com.doctoraak.doctoraakpatient.model.ClinicFilter
import com.doctoraak.doctoraakpatient.model.FavDoctorResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.PlacePickerDialog
import com.doctoraak.doctoraakpatient.ui.SignInActivity
import com.doctoraak.doctoraakpatient.ui.bookDoctor.BookDoctorActivity
import com.doctoraak.doctoraakpatient.ui.profile.ProfileActivity
import com.doctoraak.doctoraakpatient.utils.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.search_by_address.view.*
import kotlinx.android.synthetic.main.search_by_address.view.cb_insurance
import kotlinx.android.synthetic.main.search_by_city_and_area.view.*

class SearchDoctorActivity : BaseActivity() {
    private var isMedicalCenter = false
    private var specializationId = -1
    private lateinit var binding: ActivitySearchDoctorBinding
    private lateinit var filterClincs: ClinicFilter
    private lateinit var adapter: AvailableClinicsAdapter
    private var itemPosition = -1
    private val viewModel by lazy { ViewModelProvider(this).get(SearchDoctorViewModel::class.java) }

    private val SPECIALIZATION_ID_KEY = "specializtionId"
    private val CITY_ID_KEY = "cityId"
    private val AREA_ID_KEY = "areaId"
    private val LATT_KEY = "lat"
    private val LANG_ID_KEY = "lang"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SPECIALIZATION_ID_KEY, specializationId)
        outState.putInt(CITY_ID_KEY, cityId)
        outState.putInt(AREA_ID_KEY, areaId)

        if (lang != null && latt != null) {
            outState.putDouble(LATT_KEY, latt!!)
            outState.putDouble(LANG_ID_KEY, lang!!)
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
        binding.searchByAddress.et_address.setText(address)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_doctor)

        specializationId =
            intent.getIntExtra(getString(com.doctoraak.doctoraakpatient.R.string.specialization_Id_Key),
                -1)
        isMedicalCenter = intent.getBooleanExtra(getString(R.string.isMedicalCenterKey), false)

        if (savedInstanceState != null) {
            getState(savedInstanceState)
        }

        if (!SessionManager.isLogIn()) {
            binding.searchByCity.cb_insurance.hide()
        }

        val cityList = Utils.getCitiessNames()
        val areaList = Utils.getAreasNames()

        cityList.add(0, getString(R.string.select_none))
        areaList.add(0, getString(R.string.select_none))

        setupAutoCompleteTextView(binding.searchByCity.sp_city, cityList)

        if (!SessionManager.isLogIn())
            binding.searchByAddress.cb_insurance.hide()

        setListenerToRadioButtons()

        binding.searchByAddress.et_address.setOnClickListener {
            checkIfGPSEnabled()
        }

        binding.btnSearch.setOnClickListener {
            if (binding.searchByAddress.visibility == View.VISIBLE) {
                if (latt.validateLattLang(this, lang, binding.searchByAddress.etl_address)) {
                    val b: Boolean = binding.searchByAddress.cb_insurance.isChecked
                    if (Utils.checkInternetConnection(this, binding.clSearchDoctors)) {
                        filterClincs =
                            ClinicFilter(api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else "",
                                patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1,
                                insurance = b.toInt(),
                                specialization_id = specializationId,
                                latt = latt!!,
                                lang = lang!!,
                                isMedicalCenter = isMedicalCenter.toInt())
                        viewModel.getClinics(filterClincs)
                    }
                } else {
                    showSnackbar(binding.clSearchDoctors,
                        getString(R.string.please_select_location))
                }
            } else {
                val b: Boolean = binding.searchByCity.cb_insurance.isChecked
                val isValidateCity = cityId.validateCity(this, binding.searchByCity.spl_city)
                val isValidateArea = areaId.validateArea(this, binding.searchByCity.spl_area)
                if (isValidateCity && isValidateArea) {

                    filterClincs =
                        ClinicFilter(api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else "",
                            patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1,
                            insurance = b.toInt(),
                            specialization_id = specializationId,
                            city_id = cityId,
                            area_id = areaId,
                            isMedicalCenter = isMedicalCenter.toInt())

                    viewModel.getClinics(filterClincs)
                }
            }
        }

        setAdapter()
        observeData()
        autoCompleteTextViewSelectListners()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
                if (user.patient_name == "" || user.phone2 == "") {
                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                    intent.putExtra(Constants.MISSING_DATA, true)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
            finish()
        }
    }

    private fun setAdapter() {
        binding.rvClinic.layoutManager = LinearLayoutManager(this)
        adapter = AvailableClinicsAdapter(this)
        setAdapterClicks()
        binding.rvClinic.adapter = adapter
        binding.rvClinic.addItemDecoration(DividerItemDecorationExceptLast(this,
            ResourcesCompat.getDrawable(resources, R.drawable.drawable_divider, null)!!), -1)

        binding.rvClinic.onStartLoadingAction = {
            if (::filterClincs.isInitialized) {
                viewModel.getClinics(filterClincs)
                true
            }

            false
        }

        adapter.setData(viewModel.clinicsReponse.value?.data)
    }

    private fun setAdapterClicks() {
        adapter.setOnItemClickListener(object : AvailableClinicsAdapter.ClickListener {
            override fun onClick(model: Clinic, aView: View) {
                val intent = Intent(this@SearchDoctorActivity, BookDoctorActivity::class.java)
                intent.putExtra(getString(R.string.clinic_item_key),
                    Utils.convertObjectToJson(model))
                startActivity(intent)
            }

        })

        adapter.setOnItemClickFavListener(object : AvailableClinicsAdapter.ClickFavListener {
            override fun onClick(position: Int, model: Clinic, aView: View) {
                if (Utils.checkInternetConnection(this@SearchDoctorActivity,
                        binding.clSearchDoctors)) {
                    viewModel.favDoctor(Utils.getUserId(), model.doctor.id, Utils.getApiToken())
                    itemPosition = position
                    hideFavImage(position)
                    showProgressBar(position)
                    if (aView.tag.equals(Constants.FAVED)) {
                        (aView as ImageView).setImageResource(R.drawable.ic_unfav)
                        aView.tag = Constants.Not_FAVED
                        Utils.removeIdToFavList(model.doctor_id)
                    } else {
                        (aView as ImageView).setImageResource(R.drawable.ic_favorite)
                        aView.tag = Constants.FAVED
                        Utils.addIdToFavList(model.doctor_id)
                    }
                }
            }
        })
    }

    fun hideFavImage(position: Int) {
        try {
            val holder = binding.rvClinic.findViewHolderForAdapterPosition(position)
            (holder as AvailableClinicsAdapter.MyViewHolder).fav.visibility = View.INVISIBLE
        } catch (e: Exception) {
        }
    }

    fun showFavImage(position: Int) {
        try {
            val holder = binding.rvClinic.findViewHolderForAdapterPosition(position)
            (holder as AvailableClinicsAdapter.MyViewHolder).fav.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    fun hideProgressBar(position: Int) {
        try {
            val holder = binding.rvClinic.findViewHolderForAdapterPosition(position)
            (holder as AvailableClinicsAdapter.MyViewHolder).progressBar.visibility = View.INVISIBLE
        } catch (e: Exception) {
        }
    }

    fun showProgressBar(position: Int) {
        try {
            val holder = binding.rvClinic.findViewHolderForAdapterPosition(position)
            (holder as AvailableClinicsAdapter.MyViewHolder).progressBar.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    private fun setListenerToRadioButtons() {
        val motion = findViewById<MotionLayout>(R.id.motion_layout)
        binding.tvSearchByAddress.setOnClickListener {
            if (binding.searchByCity.visibility != View.GONE) {
                applyAnimation(R.anim.fade_out, binding.searchByCity)
                binding.searchByCity.visibility = View.GONE

                applyAnimation(R.anim.fade_in, binding.searchByAddress)
                binding.searchByAddress.visibility = View.VISIBLE

                motion.setTransition(R.id.start, R.id.end)
                motion.transitionToEnd()
            }
        }

        binding.tvSearchByCity.setOnClickListener {
            if (binding.searchByAddress.visibility != View.GONE) {
                applyAnimation(R.anim.fade_out, binding.searchByAddress)
                binding.searchByAddress.visibility = View.GONE

                applyAnimation(R.anim.fade_in, binding.searchByCity)
                binding.searchByCity.visibility = View.VISIBLE

                motion.setTransition(R.id.start2, R.id.end2)
                motion.transitionToEnd()
            }
        }
    }

    private fun getState(savedInstanceState: Bundle) {
        specializationId = savedInstanceState.getInt(SPECIALIZATION_ID_KEY)
        cityId = savedInstanceState.getInt(CITY_ID_KEY)
        areaId = savedInstanceState.getInt(AREA_ID_KEY)
        if (savedInstanceState.containsKey(LANG_ID_KEY) &&
            savedInstanceState.containsKey(LATT_KEY)) {
            latt = savedInstanceState.getDouble(LATT_KEY)
            lang = savedInstanceState.getDouble(LANG_ID_KEY)
        }
    }

    private fun autoCompleteTextViewSelectListners() {
        binding.searchByCity.sp_city.setOnItemClickListener { parent, view, position, id ->
            val s = parent.getItemAtPosition(position)
            areaId = -1
            binding.searchByCity.sp_area.removeSelectedItem(0)

            if (s.equals(getString(R.string.select_none))) {
                cityId = -1
            } else {
                cityId = Utils.getCityId(s.toString())
                val newAreaList = Utils.getAreasNamesForCityId(cityId)
                newAreaList.add(0, getString(R.string.select_none))
                setupAutoCompleteTextView(binding.searchByCity.sp_area, newAreaList)
            }
        }

        binding.searchByCity.sp_area.setOnItemClickListener(AdapterView.OnItemClickListener
        { parent, view, position, id ->
            val s = parent.getItemAtPosition(position)
            if (s.equals(getString(R.string.select_none))) {
                areaId = -1
            } else {
                areaId = Utils.getAreaId(s.toString())
                Log.d("saif", "area s= $s    ,id= $areaId")
            }
        })
    }

    private fun observeData() {
        viewModel.clinicsReponse.observe(this, Observer {
            Log.d("saif", "")
            showClinicList()
            adapter.setData(it.data)
        })

        viewModel.favDoctorResposne.observe(this, {
            showSnackbar(binding.clSearchDoctors, it!!.getMsg())
            if (itemPosition != -1) {
                showFavImage(itemPosition)
                hideProgressBar(itemPosition)
            }
        })

        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    binding.loading!!.visibility = View.VISIBLE
                    disableButton(binding.btnSearch)
                } else {
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.btnSearch)
                }
            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clSearchDoctors, t)
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.btnSearch)
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {

                if (t!! != 0) {
                    showSnackbar(binding.clSearchDoctors, getString(t))
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.btnSearch)
                }
            }
        })
    }

    fun showClinicList() {
        binding.rvClinic.show()
        binding.scrollView.hide()
        binding.btnSearch.hide()
    }

    fun hideClinicList() {
        binding.rvClinic.hide()
        binding.scrollView.show()
        binding.btnSearch.show()
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

    fun checkIfGPSEnabled() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayPromptForEnablingGPS(this)

        } else {
            val pl = PlacePickerDialog.newInstance()
            pl.show(supportFragmentManager, PlacePickerDialog.TAG)

            pl.listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng) {
                    onPlacePicker(address, position)
                }
            }
        }
    }

}
