package com.doctoraak.doctoraakpatient.ui.labOrder


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.AnalysisOrderAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityLapOrderBinding
import com.doctoraak.doctoraakpatient.model.LabOrderDetail
import com.doctoraak.doctoraakpatient.model.LabOrderRequest
import com.doctoraak.doctoraakpatient.model.LabOrderResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.DatePikerFragment
import com.doctoraak.doctoraakpatient.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.lap_order_request.view.*
import kotlinx.android.synthetic.main.select_lap_info.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LabOrderActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {


    private val viewModel: LabOrderViewModel by lazy {
        ViewModelProviders.of(this).get(
            LabOrderViewModel::class.java
        )
    }

    private lateinit var binding: ActivityLapOrderBinding
    private lateinit var orderAdapter: AnalysisOrderAdapter
    //arraylist which will be visible to the user
    private var analysisOrderList = ArrayList<String>()
    //arraylist which will be send with the request
    private var orderWithIds = ArrayList<LabOrderDetail>()

    private var name = ""
    private var date = ""
    private var labId: Int = -1
    private var notes = ""
    private var isImageSelected = false
    private var imagePath = ""

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date = "$year-${(month + 1)}-$dayOfMonth"
        binding.inSelectLabInfo.et_date.setText(date)
    }

    private val IMAGE_PATH_KEY = "IMAGE_PATH_KEY"
    private val NAME_KEY = "NAME_KEY"
    private val DATE_KEY = "DATE_KEY"
    private val IS_BOOKING_FOR_ME_KEY = "IS_BOOKING_FOR_ME_KEY"
    private val IS_IMAGE_SELECTED_KEY = "IS_IMAGE_SELECTED_KEY"
    private val LIST_KEY = "LIST_KEY"
    private val IDS_LIST_KEY = "IDS_LIST_KEY"
    private val ORDER_LAYOUT_VISIBILTY_KEY = "ORDER_LAYOUT_VISIBILTY_KEY"
    private val SENDINFO_VISIBILTY_KEY = "ORDER_INFO_VISIBILTY_KEY"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(IMAGE_PATH_KEY , imagePath)
        outState.putString(NAME_KEY , name)
        outState.putString(DATE_KEY , date)
        outState.putBoolean(IS_IMAGE_SELECTED_KEY , isImageSelected)
        outState.putString(LIST_KEY , Gson().toJson(analysisOrderList))
        outState.putString(IDS_LIST_KEY , Gson().toJson(orderWithIds))
        outState.putInt(ORDER_LAYOUT_VISIBILTY_KEY , binding.inLabOrderRequest.visibility)
        outState.putInt(SENDINFO_VISIBILTY_KEY , binding.inSelectLabInfo.visibility)

    }

    private fun getState(savedInstanceState: Bundle) {

        imagePath = savedInstanceState.getString(IMAGE_PATH_KEY , "")
        name = savedInstanceState.getString(NAME_KEY , "")
        date = savedInstanceState.getString(DATE_KEY , "")
        isImageSelected = savedInstanceState.getBoolean(IS_IMAGE_SELECTED_KEY , false)
        binding.inLabOrderRequest.visibility = savedInstanceState.getInt(ORDER_LAYOUT_VISIBILTY_KEY)
        binding.inSelectLabInfo.visibility = savedInstanceState.getInt(SENDINFO_VISIBILTY_KEY)


        val listType1 = object : TypeToken<ArrayList<String>>() {}.type
        val listType2 = object : TypeToken<ArrayList<LabOrderDetail>>() {}.type

        analysisOrderList = Gson().fromJson(savedInstanceState.getString(LIST_KEY) ,listType1)
        orderWithIds = Gson().fromJson(savedInstanceState.getString(IDS_LIST_KEY) ,listType2)

        if (imagePath.isNotEmpty()){
            val bm = BitmapFactory.decodeFile(imagePath)
            showSelectedImage(bm)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lap_order)

        if (savedInstanceState != null){
            getState(savedInstanceState)
        }

        observeData()

        labId = intent.getIntExtra(getString(R.string.lab_id_key), -1)

        setRecyclerviewLinearLayout(binding.inLabOrderRequest.rv_medicine)
        orderAdapter = AnalysisOrderAdapter(analysisOrderList, this)
        binding.inLabOrderRequest.rv_medicine.adapter = orderAdapter
        orderAdapter.setOnItemClickDeleteListener(object : AnalysisOrderAdapter.ClickListener {
            override fun onClick(model: String, position: Int, aView: View) {
                orderWithIds.removeAt(position)
                analysisOrderList.remove(model)
                orderAdapter.notifyItemRemoved(position)
                orderAdapter.notifyItemRangeChanged(position, orderAdapter.itemCount)
            }
        })

        binding.inSelectLabInfo.et_date.setOnClickListener {
            val dialogFragment = DatePikerFragment(false)
            dialogFragment.show(supportFragmentManager, "DatePikerlab")
        }

        setupAutoCompleteTextView(binding.inLabOrderRequest.sp_name, Utils.getAnaysisNames())
        setListenerToNextButtons()
        setListenerForAutoCompleteTextView()
        setListinerToAddOrderButton()
        setListenerToRadioButtons()
        setListenerToRadioButtonsSendOptions()
        setLisetenerToSendOrders()
        chooseImage()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun setListenerToRadioButtonsSendOptions() {
        val motion  = findViewById<MotionLayout>(R.id.motion_layout)
        binding.inSelectLabInfo.tv_book_me.setOnClickListener {

            if (binding.inSelectLabInfo.etl_name.visibility != View.GONE) {
                binding.inSelectLabInfo.etl_name.visibility = View.GONE
                binding.inSelectLabInfo.etl_phone.visibility = View.GONE
                binding.inSelectLabInfo.etl_age.visibility = View.GONE
                binding.inSelectLabInfo.gender_group.visibility = View.GONE
                motion.setTransition(R.id.start2 , R.id.end2)
                motion.transitionToEnd()
            }
        }

        binding.inSelectLabInfo.tv_book_for_another.setOnClickListener {
            if (binding.inSelectLabInfo.etl_name.visibility == View.GONE) {
                binding.inSelectLabInfo.etl_name.visibility = View.VISIBLE
                binding.inSelectLabInfo.etl_phone.visibility = View.VISIBLE
                binding.inSelectLabInfo.etl_age.visibility = View.VISIBLE
                binding.inSelectLabInfo.gender_group.visibility = View.VISIBLE

                motion.setTransition(R.id.start , R.id.end)
                motion.transitionToEnd()
            }
        }
    }

    private fun setListenerToRadioButtons() {
        val motion = findViewById<MotionLayout>(R.id.motion_layout2)

        binding.inLabOrderRequest.tv_request_medicine.setOnClickListener {
            motion.setTransition(R.id.start1, R.id.end1)
            motion.transitionToEnd()
            binding.inLabOrderRequest.group_request_medicine.show()
            binding.inLabOrderRequest.group_upload_image.hide()
            binding.inLabOrderRequest.group_divider.hide()

        }

        binding.inLabOrderRequest.tv_all.setOnClickListener {
            motion.setTransition(R.id.start2, R.id.end2)
            motion.transitionToEnd()
            binding.inLabOrderRequest.group_upload_image.show()
            binding.inLabOrderRequest.group_request_medicine.show()
            binding.inLabOrderRequest.group_divider.show()
        }

        binding.inLabOrderRequest.tv_prescription.setOnClickListener {
            motion.setTransition(R.id.start3, R.id.end3)
            motion.transitionToEnd()
            binding.inLabOrderRequest.group_request_medicine.hide()
            binding.inLabOrderRequest.group_upload_image.show()
            binding.inLabOrderRequest.group_divider.hide()

        }
    }

    private fun chooseImage() {
        binding.inLabOrderRequest.iv_take_image.setOnClickListener {
            if (isStoragePermissionGranted()) {
                selectImage(this)
            }
        }
    }


    private fun sendForOther() {
        val isValidateName = binding.inSelectLabInfo.et_name.text.toString()
            .validateName(this, binding.inSelectLabInfo.etl_name)
        val isValidateAge = binding.inSelectLabInfo.et_age.text.toString()
            .validateAge(this, binding.inSelectLabInfo.etl_age)
        val isValidatePhone = binding.inSelectLabInfo.et_phone.text.toString()
            .validatePhone(this, binding.inSelectLabInfo.etl_phone)

        val isValidateDate = date.validateDate(this, binding.inSelectLabInfo.etl_date)
        if (isValidateAge && isValidateName && isValidatePhone && isValidateDate) {
            val selectedId = binding.inSelectLabInfo.gender_group.checkedRadioButtonId
            val radioButton = findViewById<View>(selectedId) as RadioButton

            notes =
                "${binding.inSelectLabInfo.et_name.text},${binding.inSelectLabInfo.et_phone.text}," +
                        "${binding.inSelectLabInfo.et_age.text},${radioButton.text}"

            val request = LabOrderRequest(
                Utils.getApiToken(), labId, date, Utils.getUserId()
                , notes,
                imagePath, orderWithIds
            )

            if (Utils.checkInternetConnection(this, binding.clLabInfo)) {
                viewModel.createLabOrder(request)
            }
        }
    }

    private fun sendForUser() {
        if (date.validateDate(this, binding.inSelectLabInfo.etl_date)) {
            val request = LabOrderRequest(
                Utils.getApiToken(), labId, date, Utils.getUserId()
                , "",
                imagePath, orderWithIds
            )

            if (Utils.checkInternetConnection(this, binding.clLabInfo)) {
                logd(request.toString() , "vdvavjv")
                viewModel.createLabOrder(request)
            }
        }
    }

    private fun setLisetenerToSendOrders() {
        binding.inSelectLabInfo.btn_book.setOnClickListener {
            if (SessionManager.isLogIn()) {
                if (binding.inSelectLabInfo.etl_name.visibility == View.GONE) {
                    sendForUser()
                } else {
                    sendForOther()
                }
            }else{
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }
    }

    private fun setListenerForAutoCompleteTextView() {

        binding.inLabOrderRequest.sp_name.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            name = parent.getItemAtPosition(position) as String
            nameId = Utils.getAnaysisId(name)

        })
    }

    private fun setListinerToAddOrderButton() {
        binding.inLabOrderRequest.btn_add_medicine.setOnClickListener {
            if (!name.equals("")) {
                val orderId = LabOrderDetail(Utils.getAnaysisId(name))
                orderWithIds.add(orderId)

                analysisOrderList.add(name)
                orderAdapter.notifyItemInserted(analysisOrderList.size)
                InitialieViewsAndVariables()
            } else {
                showSnackbar(binding.clLabInfo, getString(R.string.choose_analysis_name))
            }

        }

    }

    private fun InitialieViewsAndVariables() {
        name = ""
        nameId = -1
        AutoCompleteSetHint(binding.inLabOrderRequest.sp_name, "")

    }

    fun AutoCompleteSetHint(view: AutoCompleteTextView, hint: String) {
        view.setText(hint)
    }

    private fun setListenerToNextButtons() {
        binding.inLabOrderRequest.btn_send_order.setOnClickListener {
            showSendLayout()
        }
    }

    fun showSendLayout() {
        if (isImageSelected || orderWithIds.size > 0) {
            setVisiblityGone(binding.inLabOrderRequest)
            applyAnimation(R.anim.fade_out, binding.inLabOrderRequest)

            setVisiblityVisible(binding.inSelectLabInfo)
            applyAnimation(R.anim.fade_in, binding.inSelectLabInfo)
        } else {
            showSnackbar(binding.clLabInfo, getString(R.string.upload_image_or_choose_from_list))
        }

    }

    override fun onBackPressed() {
        if (binding.inLabOrderRequest.visibility == View.GONE) {
            setVisiblityGone(binding.inSelectLabInfo)
            applyAnimation(R.anim.fade_out, binding.inSelectLabInfo)

            setVisiblityVisible(binding.inLabOrderRequest)
            applyAnimation(R.anim.fade_in, binding.inLabOrderRequest)
        } else
            super.onBackPressed()
    }


    private fun observeData() {
        viewModel.labOrdeResponse.observe(this, object : Observer<LabOrderResponse> {
            override fun onChanged(t: LabOrderResponse?) {
                showSuccessDialog(t!!.getMsg(), getString(R.string.order_has_been_send))
                binding.loading!!.visibility = View.INVISIBLE
                enableButton(binding.inSelectLabInfo.btn_book)

            }
        })
        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    binding.loading!!.visibility = View.VISIBLE
                    disableButton(binding.inSelectLabInfo.btn_book)
                } else {
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.inSelectLabInfo.btn_book)
                }

            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {

                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clLabInfo, t)
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.inSelectLabInfo.btn_book)
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!! != 0) {
                    showSnackbar(binding.clLabInfo, getString(t))
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.inSelectLabInfo.btn_book)
                }

            }
        })

    }

    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.cancel))

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
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)

            } else {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK  ) {
                    val file = File(currentPhotoPath);
                    val bitmap : Bitmap
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media
                            .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                    }else{
                        val source = ImageDecoder.createSource(this.contentResolver, Uri.fromFile(file))
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
                            selectedImage,
                            filePathColumn, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()

                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            imagePath = cursor.getString(columnIndex)
                            val fileSize = File(imagePath)
                            if (Utils.isSuitableImageSize(this, fileSize, binding.clLabInfo)) {
                                isImageSelected = true
                                binding.inLabOrderRequest.iv_upload.setImageURI(selectedImage)
                            }
                            cursor.close()
                        }
                    }

                }
            }
        }
    }

   /* private fun showSelectedImage() {
        binding.inLabOrderRequest.iv_upload.background = null
        binding.inLabOrderRequest.iv_upload
            .setImageBitmap(BitmapFactory.decodeFile(imagePath))
    }*/

    private fun showSelectedImage(bm: Bitmap) {
        binding.inLabOrderRequest.iv_upload.background = null
        binding.inLabOrderRequest.iv_upload
            .setImageBitmap(bm)
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else {

                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage(this@LabOrderActivity)
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}
