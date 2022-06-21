package com.doctoraak.doctoraakpatient.ui.pharmacy

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.AutoCompleteTextViewAdapter
import com.doctoraak.doctoraakpatient.adapters.MedicinesOrderAdapter
import com.doctoraak.doctoraakpatient.adapters.PharmaciesAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityPharmacyBinding
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.PlacePickerDialog
import com.doctoraak.doctoraakpatient.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.pharmacy_order_medicine.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

class PharmacyActivity : BaseActivity()
{
    private val CAMERA_REQUEST_CODE = 3
    private val viewModel by lazy { ViewModelProvider(this).get(PharmacyViewModel::class.java) }

    private lateinit var binding: ActivityPharmacyBinding
    private lateinit var adapter: MedicinesOrderAdapter
    private lateinit var pharmaciesAdapter: PharmaciesAdapter
    private lateinit var layout: LinearLayoutManager

    private var isImageSelected = false
    private var imagePath = ""
    private var name = ""
    private var type = ""
    private var typeId = -1
    private var pharmacyId = -1
    private lateinit var filterRequest :PharmacyFilterRequest
    private var list = ArrayList<MedicineOrder>()
    private var orderWithIds = ArrayList<OrderDetail>()

    private val IMAGE_PATH_KEY = "IMAGE_PATH_KEY"
    private val NAME_KEY = "NAME_KEY"
    private val TYPE_KEY = "TYPE_KEY"
    private val TYPE_ID_KEY = "TYPE_ID_KEY"
    private val IS_IMAGE_SELECTED_KEY = "IS_IMAGE_SELECTED_KEY"
    private val LIST_KEY = "LIST_KEY"
    private val IDS_LIST_KEY = "IDS_LIST_KEY"
    private val SEARCH_LAYOUT_VISIBILTY_KEY = "SEARCH_LAYOUT_VISIBILTY_KEY"
    private val ORDER_INFO_VISIBILTY_KEY = "ORDER_INFO_VISIBILTY_KEY"
    private val QUANTITY_KEY = "QUANTITY_KEY"
    private val LATT_KEY = "lat"
    private val LANG_ID_KEY = "lang"

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putString(IMAGE_PATH_KEY, imagePath)
        outState.putString(NAME_KEY, name)
        outState.putString(TYPE_KEY, type)
        outState.putInt(TYPE_ID_KEY, typeId)
        outState.putBoolean(IS_IMAGE_SELECTED_KEY, isImageSelected)
        outState.putString(LIST_KEY, Gson().toJson(list))
        outState.putString(IDS_LIST_KEY, Gson().toJson(orderWithIds))
        outState.putInt(
            SEARCH_LAYOUT_VISIBILTY_KEY,
            binding.inPharmacyOrderInfo.scrollViewContainer.visibility
        )
        outState.putInt(ORDER_INFO_VISIBILTY_KEY, binding.icPharmOrderMedicine.visibility)
        outState.putString(QUANTITY_KEY, binding.icPharmOrderMedicine.tv_quantity.text.toString())

        if (lang != null && latt != null) {
            outState.putDouble(LATT_KEY, latt!!)
            outState.putDouble(LANG_ID_KEY, lang!!)
        }

    }

    private fun getState(savedInstanceState: Bundle) {
        imagePath = savedInstanceState.getString(IMAGE_PATH_KEY, "")
        name = savedInstanceState.getString(NAME_KEY, "")
        type = savedInstanceState.getString(TYPE_KEY, "")
        typeId = savedInstanceState.getInt(TYPE_ID_KEY, -1)
        isImageSelected = savedInstanceState.getBoolean(IS_IMAGE_SELECTED_KEY, false)
        binding.inPharmacyOrderInfo.scrollViewContainer.visibility =
            savedInstanceState.getInt(SEARCH_LAYOUT_VISIBILTY_KEY)
        binding.icPharmOrderMedicine.visibility =
            savedInstanceState.getInt(ORDER_INFO_VISIBILTY_KEY)
        binding.icPharmOrderMedicine.tv_quantity.text = savedInstanceState
            .getString(QUANTITY_KEY, "1")

        val listType1 = object : TypeToken<ArrayList<MedicineOrder>>() {}.type
        val listType2 = object : TypeToken<ArrayList<OrderDetail>>() {}.type

        list = Gson().fromJson(savedInstanceState.getString(LIST_KEY), listType1)
        orderWithIds = Gson().fromJson(savedInstanceState.getString(IDS_LIST_KEY), listType2)

        if (savedInstanceState.containsKey(LANG_ID_KEY) &&
            savedInstanceState.containsKey(LATT_KEY)
        ) {
            latt = savedInstanceState.getDouble(LATT_KEY)
            lang = savedInstanceState.getDouble(LANG_ID_KEY)
        }

        if (imagePath.isNotEmpty()) {
            val bm = BitmapFactory.decodeFile(imagePath)
            showSelectedImage(bm)
        }

    }

    private fun setListenerToRadioButtons() {
        val motion = findViewById<MotionLayout>(R.id.motion_layout)

        binding.icPharmOrderMedicine.tv_request_medicine.setOnClickListener {
            motion.setTransition(R.id.start1, R.id.end1)
            motion.transitionToEnd()
            binding.icPharmOrderMedicine.group_request_medicine.show()
            binding.icPharmOrderMedicine.group_upload_image.hide()
            binding.icPharmOrderMedicine.group_divider.hide()
        }

        binding.icPharmOrderMedicine.tv_all.setOnClickListener {
            motion.setTransition(R.id.start2, R.id.end2)
            motion.transitionToEnd()
            binding.icPharmOrderMedicine.group_upload_image.show()
            binding.icPharmOrderMedicine.group_request_medicine.show()
            binding.icPharmOrderMedicine.group_divider.show()
        }

        binding.icPharmOrderMedicine.tv_prescription.setOnClickListener {
            motion.setTransition(R.id.start3, R.id.end3)
            motion.transitionToEnd()
            binding.icPharmOrderMedicine.group_request_medicine.hide()
            binding.icPharmOrderMedicine.group_upload_image.show()
            binding.icPharmOrderMedicine.group_divider.hide()

        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        (supportFragmentManager.findFragmentByTag(PlacePickerDialog.TAG)
                as PlacePickerDialog?)?.let {
            it.listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng) {
                    onPlacePicker(address, position)
                }
            }
        }
    }

    fun onPlacePicker(address: String, position: LatLng) {
        latt = position.latitude
        lang = position.longitude
        binding.inPharmacyOrderInfo.searchByAddress.etAddress
            .setText(address)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_pharmacy)

        if (savedInstanceState != null) {
            getState(savedInstanceState)
        }

        observeData()
        initUI()
        setIncreaeseAndDecreaseArrowDirection()
        setupAutoCompleteTextView(binding.icPharmOrderMedicine.et_name, Utils.getMedicinesNames())
        setupAutoCompleteTextView(binding.icPharmOrderMedicine.sp_type, Utils.getMedicinesTypeNames())

        setRecyclerviewLayout()
        if (!SessionManager.isLogIn()) {
            binding.inPharmacyOrderInfo.searchByAddress.cbInsurance.hide()
        }

        adapter = MedicinesOrderAdapter(list, this)
        binding.icPharmOrderMedicine.rv_medicine.adapter = adapter

        binding.inPharmacyOrderInfo.btnNext.setOnClickListener {
            pharmaciesAdapter.clearData()
            onFilterPharmacyClick()
        }

        binding.inPharmacyOrderInfo.searchByAddress.etAddress.setOnClickListener {
            checkIfGPSEnabled()
        }

        binding.icPharmOrderMedicine.iv_decrease_quantity.setOnClickListener {
            val quantity = (binding.icPharmOrderMedicine.tv_quantity.text.toString()).toInt()
            if (quantity != 1) {
                binding.icPharmOrderMedicine.tv_quantity.text = (quantity - 1).toString()
            }
        }
        binding.icPharmOrderMedicine.iv_increase_quantity.setOnClickListener {
            val quantity = (binding.icPharmOrderMedicine.tv_quantity.text.toString()).toInt()
            binding.icPharmOrderMedicine.tv_quantity.text = (quantity + 1).toString()
        }

        setListinerToAddOrderButton()
        setAdapterDeleteListener()

        setAutoCompleteListenerForNames()
        setAutoCompleteListenerForType()
        setListenerToRadioButtons()

        binding.icPharmOrderMedicine.iv_take_image.setOnClickListener {
            if (isStoragePermissionGranted()) {
                selectImage(this)
            }
        }
        handleSearchWithNameCityAddressIndicator(binding.inPharmacyOrderInfo.searchIndicator
            , binding.inPharmacyOrderInfo.searchByCity, binding.inPharmacyOrderInfo.searchByAddress, onNameClick = {
                if (viewModel.isLoadingPharmacies.value!!)
                    binding.inPharmacyOrderInfo.searchByCity.pbLoadingNames.show()
                else
                    binding.inPharmacyOrderInfo.searchByCity.pbLoadingNames.hide()
            }, onCityClick = {
                binding.inPharmacyOrderInfo.searchByCity.pbLoadingNames.hide()
            })
        setListenerToSendButtons()
    }

    private fun onFilterPharmacyClick()
    {
        if (binding.inPharmacyOrderInfo.searchByAddress.root.isVisible)
        {
            val insurrance = binding.inPharmacyOrderInfo.searchByAddress.cbInsurance.isChecked
            val delivery = binding.inPharmacyOrderInfo.searchByAddress.cbDelivery.isChecked

            val isValidateLattLang = latt.validateLattLang(
                this, lang,
                binding.inPharmacyOrderInfo.searchByAddress.etlAddress
            )
            if (isValidateLattLang) {
                filterRequest = PharmacyFilterRequest(
                    latt = latt!!, lang = lang!!
                    , api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else ""
                    , patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1
                    , insurance = insurrance.toInt(), delivery = delivery.toInt()
                )
                if (Utils.checkInternetConnection(this, binding.clPharmacy)) {
                    getFilteredPharmacies()
                }
            } else {
                Utils.showSnackbar(
                    binding.clPharmacy, getString(R.string.please_select_location))
            }
        }
        else
        {
            val insurrance = binding.inPharmacyOrderInfo.searchByCity.cbInsurance.isChecked
            val delivery = binding.inPharmacyOrderInfo.searchByCity.cbDelivery.isChecked

            if (binding.inPharmacyOrderInfo.searchByCity.splLabName.visibility == View.VISIBLE )
            {
                val pharmacyName = binding.inPharmacyOrderInfo.searchByCity.spLabName.text.toString().trim()
                Log.d("saif", "onFilterPharmacyClick: name= $pharmacyName")
                if (pharmacyName.isEmpty())
                {
                    binding.inPharmacyOrderInfo.searchByCity.splLabName.error = getString(R.string.enter_your_name)
                    return
                }
                else
                    binding.inPharmacyOrderInfo.searchByCity.splLabName.error = null

                filterRequest = PharmacyFilterRequest(name = pharmacyName
                    , api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else ""
                    , patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1
                    , insurance = insurrance.toInt(), delivery = delivery.toInt())
                if (Utils.checkInternetConnection(this, binding.clPharmacy)) {
                    getFilteredPharmacies()
                }
            } else {
                val isValidateCity =
                    cityId.validateCity(this, binding.inPharmacyOrderInfo.searchByCity.splCity)

                if (isValidateCity)
                {
                    filterRequest = PharmacyFilterRequest(
                        city = cityId, area = areaId
                        , api_token = if (SessionManager.isLogIn()) Utils.getApiToken() else ""
                        , patient_id = if (SessionManager.isLogIn()) Utils.getUserId() else -1
                        , insurance = insurrance.toInt(), delivery = delivery.toInt()
                    )
                    if (Utils.checkInternetConnection(this, binding.clPharmacy)) {
                        getFilteredPharmacies()
                    }
                }

            }

        }
    }

    private fun getFilteredPharmacies()
    {
        viewModel.getFilteredPharmacies(filterRequest,
            { toggleLoading(it) },
            { showMessage(it) },
            { showMessage(getString(it)) })
    }

    private fun showMessage(it: String) {
        showSnackbar(binding.clPharmacy, it)
    }

    private fun toggleLoading(it: Boolean) {
        if (it) {
            binding.inPharmacyOrderInfo.btnNext.disable()
            binding.loading.show()
        } else {
            binding.inPharmacyOrderInfo.btnNext.enable()
            binding.loading.hide()
        }
    }

    private fun initUI()
    {
        binding.inPharmacyOrderInfo.searchByCity.splLabName.hint = getString(R.string.pharmacy_name)

        val cityList = Utils.getCitiessNames()
        val areaList = Utils.getAreasNames()

        cityList.add(0, getString(R.string.select_none))
        areaList.add(0, getString(R.string.select_none))

        setupAutoCompleteTextView(binding.inPharmacyOrderInfo.searchByCity.spCity, cityList)
        setupAutoCompleteTextView(binding.inPharmacyOrderInfo.searchByCity.spArea, areaList)

        binding.inPharmacyOrderInfo.searchByCity.spCity.setOnItemClickListener(AdapterView.OnItemClickListener
        { parent, view, position, id ->
            val s = parent.getItemAtPosition(position) as String
            areaId = -1
            binding.inPharmacyOrderInfo.searchByCity.spArea.removeSelectedItem(0)

            if (s.equals(getString(R.string.select_none))) {
                cityId = -1
            } else {
                cityId = Utils.getCityId(s)
                val newAreaList = Utils.getAreasNamesForCityId(cityId)
                newAreaList.add(0, getString(R.string.select_none))
                setupAutoCompleteTextView(binding.inPharmacyOrderInfo.searchByCity.spArea, newAreaList)
            }
        })

        binding.inPharmacyOrderInfo.searchByCity.spArea.setOnItemClickListener(AdapterView.OnItemClickListener
        { parent, view, position, id ->
            val s = parent.getItemAtPosition(position) as String
            if (s.equals(getString(R.string.select_none))) {
                areaId = -1
            } else {
                areaId = Utils.getAreaId(s)
            }
        })

        setRecyclerviewLinearLayout(binding.rvPharmacies)
        pharmaciesAdapter = PharmaciesAdapter(this)
        binding.rvPharmacies.adapter = pharmaciesAdapter
        binding.rvPharmacies.onStartLoadingAction = {
            if (::filterRequest.isInitialized)
            {
                getFilteredPharmacies()
                true
            }

            false
        }

        pharmaciesAdapter.setOnItemClickListener(object : PharmaciesAdapter.ClickListener {
            override fun onClick(model: Pharmacy) {
                pharmacyId = model.id

                binding.icPharmOrderMedicine.show()
                applyAnimation(R.anim.enter_translate, binding.icPharmOrderMedicine)
                applyAnimation(R.anim.fade_out, binding.rvPharmacies)
                binding.rvPharmacies.hide()
            }
        })
    }

    private fun setIncreaeseAndDecreaseArrowDirection() {
        if (Utils.getAppLanguage().equals("ar")) {
            binding.icPharmOrderMedicine.iv_decrease_quantity.rotation = 180f
            binding.icPharmOrderMedicine.iv_increase_quantity.rotation = 0f

        }
    }

    private fun setListenerToSendButtons()
    {
        binding.icPharmOrderMedicine.btn_send_order.setOnClickListener {

            if (isImageSelected || orderWithIds.size > 0) {

                if (Utils.checkInternetConnection(this, binding.clPharmacy)) {

                    val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
                    if (SessionManager.isLogIn()) {
                        val request = MedicineOrderRequest(
                            Utils.getApiToken(),
                            Utils.getUserId(),
                            binding.icPharmOrderMedicine.et_notes.text.toString(),
                            "",
                            orderWithIds, date, pharmacyId)
                        viewModel.createPharmacyOrder(request, imagePath)
                    } else {
                        showLoginFirstDialog(getString(R.string.login_first))
                    }
                }
            } else {
                showSnackbar(
                    binding.clPharmacy, getString(R.string.upload_image_or_choose_from_list)
                )
            }
        }
    }

    private fun setAutoCompleteListenerForNames() {
        binding.icPharmOrderMedicine.et_name.setOnItemClickListener(AdapterView.OnItemClickListener
        { parent, view, position, id ->
            name = parent.getItemAtPosition(position) as String
            nameId = Utils.getMedicineId(name)
        })
    }

    private fun setAutoCompleteListenerForType() {
        binding.icPharmOrderMedicine.sp_type.setOnItemClickListener(AdapterView.OnItemClickListener
        { parent, view, position, id ->
            type = parent.getItemAtPosition(position) as String
            typeId = Utils.getMedicineTypeId(type)

        })
    }

    private fun setListinerToAddOrderButton() {
        binding.icPharmOrderMedicine.btn_add_medicine.setOnClickListener {
            if (!name.equals("") && !type.equals("")) {
                val orderId = OrderDetail(
                    Utils.getMedicineId(name), Utils.getMedicineTypeId(type),
                    (binding.icPharmOrderMedicine.tv_quantity.text.toString()).toInt()
                )
                orderWithIds.add(orderId)

                val order = MedicineOrder(
                    name, type
                    , binding.icPharmOrderMedicine.tv_quantity.text.toString()
                )

                list.add(order)
                adapter.notifyItemInserted(list.size)
                InitialieViewsAndVariables()
            } else {
                showSnackbar(binding.clPharmacy, getString(R.string.please_choose_name_and_type))
            }

        }

    }

    private fun InitialieViewsAndVariables() {
        name = ""
        type = ""
        nameId = -1
        typeId = -1
        binding.icPharmOrderMedicine.tv_quantity.text = "1"
        AutoCompleteSetHint(binding.icPharmOrderMedicine.et_name, "")
        AutoCompleteSetHint(binding.icPharmOrderMedicine.sp_type, "")
    }

    private fun setAdapterDeleteListener() {
        adapter.setOnItemClickDeleteListener(object : MedicinesOrderAdapter.ClickListener {
            override fun onClick(model: MedicineOrder, position: Int, aView: View) {
                orderWithIds.removeAt(position)
                list.remove(model)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, adapter.itemCount - position)
            }
        })
    }

    fun AutoCompleteSetHint(view: AutoCompleteTextView, hint: String) {
        view.setText(hint)
    }

    private fun setRecyclerviewLayout() {
        layout = LinearLayoutManager(this)
        layout.reverseLayout = true
        binding.icPharmOrderMedicine.rv_medicine.layoutManager = layout
    }

    override fun onBackPressed()
    {
        if (binding.inPharmacyOrderInfo.root.isVisible)
            super.onBackPressed()
        else if (binding.rvPharmacies.isVisible)
        {
            applyAnimation(R.anim.activity_close_translate, binding.rvPharmacies)
            binding.rvPharmacies.hide()

            applyAnimation(R.anim.fade_in, binding.inPharmacyOrderInfo.root)
            binding.inPharmacyOrderInfo.root.show()
        }
        else if (binding.icPharmOrderMedicine.isVisible) {
            binding.icPharmOrderMedicine.visibility = View.GONE
            applyAnimation(R.anim.activity_close_translate, binding.icPharmOrderMedicine)

            applyAnimation(R.anim.fade_in, binding.rvPharmacies)
            binding.rvPharmacies.show()
        } else {
            applyAnimation(R.anim.activity_close_translate, binding.rvPharmacies)
            binding.rvPharmacies.hide()

            binding.inPharmacyOrderInfo.root.visibility = View.VISIBLE
            applyAnimation(R.anim.fade_in, binding.inPharmacyOrderInfo.root)
        }
    }

    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_photo), getString(R.string.choose_from_gallery)
            , getString(R.string.cancel))

        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.select_image))

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item].equals(getString(R.string.take_photo))) {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.doctoraak.doctoraakpatient.fileprovider", photoFile
                    )
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePicture, 0)
                }

            } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)

            } else {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK) {
                    val file = File(currentPhotoPath)
                    val bitmap: Bitmap
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media
                            .getBitmap(this.contentResolver, Uri.fromFile(file))
                    } else {
                        val source =
                            ImageDecoder.createSource(this.contentResolver, Uri.fromFile(file))
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    isImageSelected = true
                    showSelectedImage(bitmap)
                    imagePath = currentPhotoPath

                }
                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = data.data

                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor = contentResolver.query(
                            selectedImage, filePathColumn, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()

                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            imagePath = cursor.getString(columnIndex)
                            val fileSize = File(imagePath)
                            if (Utils.isSuitableImageSize(this, fileSize, binding.clPharmacy)) {
                                isImageSelected = true
                                binding.icPharmOrderMedicine.iv_upload.setImageURI(selectedImage)
                            }
                            cursor.close()
                        }
                    }

                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun showSelectedImage(bm: Bitmap) {
        binding.icPharmOrderMedicine.iv_upload.background = null
        binding.icPharmOrderMedicine.iv_upload
            .setImageBitmap(bm)
    }

    private fun observeData()
    {
        viewModel.pharmacyOrdeResponse.observe(this, object : Observer<PharmacyOrderResponse> {
            override fun onChanged(t: PharmacyOrderResponse?) {
                binding.loading!!.visibility = View.INVISIBLE
                binding.icPharmOrderMedicine.btn_send_order.enable()
                showSuccessDialog(t!!.getMsg(), getString(R.string.order_has_been_send))
            }
        })

        viewModel.pharmacyFilterResponse.observe(this, Observer {
            if (it.data.size > 0) {
                pharmaciesAdapter.setData(it.data)
                binding.rvPharmacies.show()
                binding.inPharmacyOrderInfo.root.hide()
            }
            else {
                if (!binding.rvPharmacies.isVisible)
                    showSnackbar(binding.clPharmacy, getString(R.string.no_pharmacy_found))
            }
        })

        viewModel.isLoadingPharmacies.observe(this, Observer {
            if (!binding.inPharmacyOrderInfo.searchByCity.splLabName.isVisible)
                return@Observer

            if (viewModel.isLoadingPharmacies.value!!)
                binding.inPharmacyOrderInfo.searchByCity.pbLoadingNames.show()
            else
                binding.inPharmacyOrderInfo.searchByCity.pbLoadingNames.hide()
        })

        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    binding.loading!!.visibility = View.VISIBLE
                    binding.icPharmOrderMedicine.btn_send_order.disable()
                } else {
                    binding.loading!!.visibility = View.INVISIBLE
                    binding.icPharmOrderMedicine.btn_send_order.enable()
                }

            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clPharmacy, t)
                    binding.loading!!.visibility = View.INVISIBLE
                    binding.icPharmOrderMedicine.btn_send_order.enable()
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!! != 0) {
                    showSnackbar(binding.clPharmacy, getString(t))
                    binding.loading!!.visibility = View.INVISIBLE
                    binding.icPharmOrderMedicine.btn_send_order.enable()
                }
            }
        })

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

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST_CODE
                )
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                selectImage(this@PharmacyActivity)
            }
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }



}
