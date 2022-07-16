package com.doctoraak.doctoraakpatient.ui.signUp

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.customView.DialogType
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import com.doctoraak.doctoraakpatient.databinding.ActivitySignUpBinding
import com.doctoraak.doctoraakpatient.model.User
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.SignInActivity
import com.doctoraak.doctoraakpatient.ui.main.MainActivity
import com.doctoraak.doctoraakpatient.utils.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var insurancId = -1
    private val INSURANCEID_KEY = "INSURANCEID_KEY"

    private val signUpViewModel by lazy {
        ViewModelProviders.of(this).get(SignUpViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(INSURANCEID_KEY , insurancId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        if (savedInstanceState != null){
            getState(savedInstanceState)
        }
        setInsurranseList()
        binding.lifecycleOwner = this
        binding.signUpViewModel = signUpViewModel
        binding.clickHandler = SignUpClickHandler()

        if (SessionManager.isInMobileVerfi()) {
            binding.apply {
                mobileVerification.viewGroup.visibility = VISIBLE
                mainSignUp.visibility = GONE
                ivEditPhoto.visibility = GONE
                ivProfile.visibility = GONE
            }

           // binding.mobileVerification.viewGroup.login_mob_ver.visibility = View.VISIBLE
        }

        movingCursorToNext()
        observeViewModel()
    }

    private fun movingCursorToNext()
    {
        val handleEditText: (EditText, EditText)->Unit = { et, nextEt ->
            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                    if (!text.isNullOrBlank())
                        nextEt.requestFocus(0)
                }
            })
        }

        with(binding.mobileVerification)
        {
            handleEditText(etSmsCode, etSmsCode2)
            handleEditText(etSmsCode2, etSmsCode3)
            handleEditText(etSmsCode3, etSmsCode4)
            handleEditText(etSmsCode4, etSmsCode5)
        }
    }

    private fun getState(savedInstanceState: Bundle) {
        insurancId = savedInstanceState.getInt(INSURANCEID_KEY , -1)
    }

    private fun setInsurranseList() {
        val list = Utils.getInsurranceNames()
        list.add(0 , getString(R.string.select_none))
        setupAutoCompleteTextView(binding.etInsurrance, list)
        binding.etInsurrance.setOnItemClickListener { adapterView, view, position, l ->
            if (position == 0){
                insurancId = -1
            }else{
                val name = adapterView.getItemAtPosition(position) as String
                insurancId = Utils.getInsurranceId(name)
            }

        }
    }

    private fun observeViewModel() {
        signUpViewModel.isLoading.observe(this, Observer {
            if (it) startLoading() else stopLoading()
        })

        signUpViewModel.errorInt.observe(this,
            Observer { showSnackbar(binding.frSignup, getString(it)) })

        signUpViewModel.errorMsg.observe(this, Observer { showSnackbar(binding.frSignup, it) })

        signUpViewModel.userResponse.observe(this, Observer {
            binding.apply {
                ivEditPhoto.visibility = GONE
                ivProfile.visibility = GONE
            }
            SessionManager.saveUserIDMobileVerfi(it.user!!.id.toString())
            binding.mainSignUp.animateOutInX(binding.mobileVerification.viewGroup)
        })

        signUpViewModel.mobileVerification.observe(this, Observer {
            SessionManager.removeMobileVerfiMode()
            SessionManager.logIn(it.user!!)
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
            finish()
        })

        signUpViewModel.resendSmsReponse.observe(this, Observer {
            showSnackbar(binding.frSignup, getString(R.string.sms_send))
        })
    }

    private fun startLoading() {
        binding.btnSignUp.isEnabled = false
        binding.mobileVerification.btnConfirmPhone.isEnabled = false
        binding.loading.visibility = VISIBLE
    }

    private fun stopLoading() {
        binding.btnSignUp.isEnabled = true
        binding.mobileVerification.btnConfirmPhone.isEnabled = true
        binding.loading.visibility = GONE
    }

    inner class SignUpClickHandler {

        fun onProfileImageClick() {
            if (isStoragePermissionGranted()) {
                selectImage(this@SignUpActivity)
            }
        }

        fun onBirthdayClick() {
            val calendar = Calendar.getInstance()

            DatePickerDialog(this@SignUpActivity,
                DatePickerDialog.OnDateSetListener { v, year, month, dayOfMonth ->
                    // mus be this format:   2018-08-11
                    binding.etBirthday.setText("$year-${(month + 1)}-$dayOfMonth")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        fun onFinishAndBackClick() = finishAndBack()

        fun onTermsClick() {
            signUpViewModel.termsAndConditions()
        }

        fun onLoginTextClick() {
            showWrringDialog()
        }

        fun onSignUpClick(user: User) {
            val birthday = user.birthdate.validateEmpty(this@SignUpActivity, binding.etlBirthday)
            val name = user.name.validateFullName(this@SignUpActivity, binding.etlName)
            val address = user.address.validateEmptyAddress(this@SignUpActivity, binding.etlAddress)
            val password = user.password.validatePassword(this@SignUpActivity, binding.etlPassword)
            val email = user.email.validateEmail(this@SignUpActivity, binding.etlEmail)
            val phone = user.phone.validatePhone(this@SignUpActivity, binding.etlPhone)
            var insuranceCode = true
            binding.etlInsurranceCode.error = null
            if (insurancId != -1) {
                insuranceCode = binding.etInsurranceCode.text.toString()
                    .validateInsurranceCode(this@SignUpActivity, binding.etlInsurranceCode)
            }

            if (!binding.cbTerms.isChecked) {
                showSnackbar(binding.frSignup, getString(R.string.approve_terms_and_conditions))
                binding.cbTerms.setTextColor(
                    ResourcesCompat.getColor(resources, R.color.red,null))
            } else
                binding.cbTerms.setTextColor(
                    ResourcesCompat.getColor(resources, R.color.black, null))

            if (birthday && name && address && password && email && phone
                && binding.cbTerms.isChecked && insuranceCode) {
                val selectedId = (binding.genderGroup as RadioGroup).checkedRadioButtonId
                val radioButton = findViewById<View>(selectedId) as RadioButton

                signUpViewModel.user.value?.gender =
                    if (radioButton.text.equals(getString(R.string.male))) "male" else "female"

                signUpViewModel.user.value?.insuranceId = insurancId
                signUpViewModel.user.value?.insuranceCode = binding.etInsurranceCode.text.toString()

                if (Utils.checkInternetConnection(this@SignUpActivity, binding.frSignup))
                    signUpViewModel.signUp()
            }
        }

        fun onConfirmPhoneClick() {
            val verCode = binding.mobileVerification.etSmsCode.text.toString()
            val code2 = binding.mobileVerification.etSmsCode2.text.toString()
            val code3 = binding.mobileVerification.etSmsCode3.text.toString()
            val code4 = binding.mobileVerification.etSmsCode4.text.toString()
            val code5 = binding.mobileVerification.etSmsCode5.text.toString()

            val code = "$verCode$code2$code3$code4$code5"
            if (code.trim().length == 5 ){
                if (Utils.checkInternetConnection(this@SignUpActivity, binding.frSignup)) {
                    signUpViewModel.confirmPhone(code)
                }
            }else{
                toast("Please enter verification code" )
            }

        }

        fun onResendSmsCodeClick() {
            var id: Int? = null
            if (SessionManager.isInMobileVerfi()) {
                id = SessionManager.getUserIDMobileVerfi()!!.toInt()
            }
            if (id != null) {
                if (Utils.checkInternetConnection(this@SignUpActivity, binding.frSignup)) {
                    signUpViewModel.resendSmsCode(id)
                }
            }
        }
    }

    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(
            getString(com.doctoraak.doctoraakpatient.R.string.take_photo), getString(
                com.doctoraak.doctoraakpatient.R.string.choose_from_gallery
            )
            , getString(com.doctoraak.doctoraakpatient.R.string.cancel)
        )

        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(com.doctoraak.doctoraakpatient.R.string.select_image))

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item].equals(getString(com.doctoraak.doctoraakpatient.R.string.take_photo))) {
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

            } else if (options[item].equals(getString(com.doctoraak.doctoraakpatient.R.string.choose_from_gallery))) {
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

                    signUpViewModel.image(currentPhotoPath)
                    binding.ivProfile.background = null
                    binding.ivProfile.setImageBitmap(bitmap)

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
                            val imagePath = cursor.getString(columnIndex)
                            val fileSize = File(imagePath)
                            if (Utils.isSuitableImageSize(this, fileSize, binding.frSignup)) {
                                signUpViewModel.image(imagePath)
                                //binding.ivProfile.setImageBitmap(BitmapFactory.decodeFile(imagePath))
                                binding.ivProfile.setImageURI(selectedImage)
                            }
                            cursor.close()
                        }
                    }

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
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage(this@SignUpActivity)
        }
    }

    fun showWrringDialog() {
        val sd = SweetDialog.newInstance(this, DialogType.WARNING)
        sd.show()
        sd.setMessage(getString(R.string.are_you_to_go_to_login))
        sd.setTitle(getString(R.string.warning))
        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })
        sd.setOkClickListener(View.OnClickListener {
            SessionManager.removeMobileVerfiMode()
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }
        )
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
