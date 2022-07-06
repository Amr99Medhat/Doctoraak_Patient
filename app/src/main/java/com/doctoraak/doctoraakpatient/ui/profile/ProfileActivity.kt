package com.doctoraak.doctoraakpatient.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import com.doctoraak.doctoraakpatient.databinding.ActivityProfileBinding
import com.doctoraak.doctoraakpatient.model.UpdatedProfileRequest
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.DatePikerFragment
import com.doctoraak.doctoraakpatient.ui.main.MainActivity
import com.doctoraak.doctoraakpatient.utils.Constants
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.dialog_sweet.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    var name = ""
    var photo = ""
    var gender = ""
    var patinet_name = ""
    var phone2 = ""


    private val viewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(
            ProfileViewModel::class.java
        )
    }

    private lateinit var binding: ActivityProfileBinding

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.tvBirthday.text = "$year-${(month + 1)}-$dayOfMonth"
        showSaveButton("$year-${(month + 1)}-$dayOfMonth")
    }


    enum class DialogType {
        Text,
        Number
    }

    private val NAME_KEY = "NAME_KEY"
    private val PHOTO_KEY = "PHOTO_KEY"
    private val GENDER_KEY = "GENDER_KEY"
    private val BUTTON_VISIBILITY_KEY = "BUTTON_VISIBILITY_KEY"


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(NAME_KEY, name)
        outState.putString(PHOTO_KEY, photo)
        outState.putString(GENDER_KEY, gender)
        outState.putInt(BUTTON_VISIBILITY_KEY, binding.btnSave.visibility)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_profile
        )

        val missing = intent.getBooleanExtra(Constants.MISSING_DATA, false)
        if (missing) {
            showMissingDataDialog()
        }

        if (savedInstanceState == null) {
            initializeData()
            viewModel.user.value = Utils.getUser()
        } else {
            getCurrentStat(savedInstanceState)
        }
        binding.lifecycleOwner = this
        binding.userViewModel = viewModel
        binding.clickHander = ProfileClickHander()
        iniatializeUI()

        binding.btnSave.setOnClickListener {
            if (isValidProfileDetails()) {
                showWrringDialog()

            }
        }
        observeData()
        setListenerToInfoButtons()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun getCurrentStat(savedInstanceState: Bundle) {
        name = savedInstanceState.getString(NAME_KEY, "")
        photo = savedInstanceState.getString(PHOTO_KEY, "")
        gender = savedInstanceState.getString(GENDER_KEY, "")
        binding.btnSave.visibility = savedInstanceState.getInt(BUTTON_VISIBILITY_KEY)
    }

    private fun initializeData() {
        val user = Utils.getUser()
        name = Utils.getUserName()
        gender = user.gender
        photo = user.photo
        patinet_name = user.patient_name
        phone2 = user.phone2
    }

    private fun iniatializeUI() {
        Glide.with(this).load(photo)
            .placeholder(R.drawable.ic_face)
            .error((R.drawable.ic_face))
            .into(binding.civImage)

        binding.tvName.text = Utils.getUserName()
        binding.tvEmail.text = viewModel.user.value!!.email
        binding.tvGender.text =
            if (gender.equals("male")) getString(R.string.male) else getString(R.string.female)

    }

    private fun setListenerToInfoButtons() {

        binding.ivEditName.setOnClickListener {
            showInputDialog(DialogType.Text, getString(R.string.enter_your_name))
        }
    }

    private fun observeData() {

        viewModel.updateProfileResponse.observe(this
        ) { t ->
            SessionManager.logIn(t!!.user!!)
            showSuccessDialog(
                getString(R.string.profile_updated_successfulty),
                getString(R.string.done)
            )
            binding.loading.visibility = View.INVISIBLE
            enableButton(binding.btnSave)
            setVisiblityGone(binding.btnSave)
        }
        viewModel.isLoading.observe(this) { t ->
            if (t!!) {
                binding.loading.visibility = View.VISIBLE
                disableButton(binding.btnSave)
            } else {
                binding.loading.visibility = View.INVISIBLE
                enableButton(binding.btnSave)
            }
        }

        viewModel.errorMsg.observe(this) { t ->
            if (!t.isNullOrEmpty()) {
                showSnackbar(binding.clProfile, t)
                binding.loading.visibility = View.INVISIBLE
                enableButton(binding.btnSave)
            }
        }

        viewModel.errorInt.observe(this) { t ->
            if (t!! != 0) {
                showSnackbar(binding.clProfile, getString(t))
                binding.loading.visibility = View.INVISIBLE
                enableButton(binding.btnSave)
            }
        }
    }

    private fun showInputDialog(type: DialogType, hint: String) {
        val builder = AlertDialog.Builder(this@ProfileActivity)

        val inflater = this@ProfileActivity.getLayoutInflater()
        val mView = inflater.inflate(R.layout.profile_dialog, null)
        val edtext = mView.findViewById(R.id.ed_data)
                as TextInputEditText

        if (type == DialogType.Number){
            edtext.inputType = InputType.TYPE_CLASS_NUMBER
            edtext.filters = arrayOf<InputFilter>(LengthFilter(11))
        }
        val edltext = mView.findViewById(R.id.edl_data)
                as TextInputLayout

        edltext.hint = hint
        if (type == DialogType.Number) {
            edtext.inputType = InputType.TYPE_CLASS_NUMBER
        } else {
            edtext.inputType = InputType.TYPE_CLASS_TEXT
        }
        builder.setView(mView).setPositiveButton(getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, id ->
                val text = edtext.text.toString()
                showSaveButton(text)
                if (hint.equals(getString(R.string.enter_your_name))) {
                    patinet_name = text
                    binding.tvFulName.text = patinet_name
                } else if (hint.equals(getString(R.string.enter_phone))) {
                    phone2 = text

                    binding.tvSecondPhone.text = phone2
                } else {
                    //viewModel.user.value!!.address = text
                    binding.tvAddress.text = text
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkFields()
                }
            })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showSaveButton(text: String) {
        if (binding.btnSave.visibility != View.VISIBLE && text.isNotEmpty())
            binding.btnSave.visibility = View.VISIBLE
    }

    private fun showGenderDialog() {
        val builder = AlertDialog.Builder(
            this@ProfileActivity
        )
        val inflater = this@ProfileActivity.getLayoutInflater()
        val mView =
            inflater.inflate(com.doctoraak.doctoraakpatient.R.layout.profile_dialog_gender, null)
        val edtext = mView.findViewById(R.id.sp_gender)
                as AutoCompleteTextView
        var temp = binding.tvGender.text.toString()

        setupAutoCompleteTextView(edtext,
            arrayListOf(getString(R.string.male), getString(R.string.female)))
        edtext.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            temp = if (selectedItem.equals(getString(R.string.male))) {
                "male"
            } else {
                "female"
            }

        })
        builder.setView(mView)
            .setPositiveButton(getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, id ->
                    gender = temp
                    binding.tvGender.text = if (temp.equals("male"))
                        getString(R.string.male)
                    else getString(R.string.female)

                    showSaveButton(gender)
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, id -> gender = "";dialog.cancel() })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_photo),
            getString(R.string.choose_from_gallery),
            getString(R.string.cancel)
        )

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
                0 -> if (resultCode == Activity.RESULT_OK) {

                    val file = File(currentPhotoPath);
                    val bitmap: Bitmap
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media
                            .getBitmap(this.getContentResolver(), Uri.fromFile(file))
                    } else {
                        val source =
                            ImageDecoder.createSource(this.contentResolver, Uri.fromFile(file))
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    binding.civImage.setImageBitmap(bitmap)
                    showSaveButton("image loaded")
                    photo = currentPhotoPath

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
                            photo = cursor.getString(columnIndex)
                            binding.civImage.setImageURI(selectedImage)
                            showSaveButton("image loaded")
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
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
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
            selectImage(this@ProfileActivity)
        }
    }

    override fun showSuccessDialog(msg: String, title: String) {
        val sd = SweetDialog.newInstance(
            this,
            com.doctoraak.doctoraakpatient.customView.DialogType.SUCCESS
        )
        sd.show()
        sd.setMessage(msg)
        sd.setTitle(title)
        sd.btn_cancel.visibility = View.GONE
        sd.setCancelable(false)
        sd.setOkClickListener(View.OnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        )
    }

    fun showWrringDialog() {
        val sd = SweetDialog.newInstance(
            this,
            com.doctoraak.doctoraakpatient.customView.DialogType.WARNING
        )
        sd.show()
        sd.setMessage(getString(R.string.are_sure_you_to_update_profile))
        sd.setTitle(getString(R.string.warning))
        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })
        sd.setOkClickListener(View.OnClickListener {
            val request = UpdatedProfileRequest(
                Utils.getUserId().toString(),
                name,
                gender,
                viewModel.user.value!!.birthdate,
                photo,
                viewModel.user.value!!.address,
                Utils.getApiToken(),
                patinet_name,
                phone2
            )

            if (Utils.checkInternetConnection(this, binding.clProfile)) {
                viewModel.updateProfile(request)
            }
            sd.dismiss()
        }
        )
    }

    fun showMissingDataDialog() {
        val sd = SweetDialog.newInstance(
            this,
            com.doctoraak.doctoraakpatient.customView.DialogType.ERROR
        )
        sd.show()
        sd.setMessage(getString(R.string.please_complete_your_profile))
        sd.setTitle(getString(R.string.warning))
        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })
        sd.setOkClickListener(View.OnClickListener {
            sd.dismiss()
        }
        )
    }

    private fun showUnValidProfileDialog(errorMessage: String) {
        val sd = SweetDialog.newInstance(
            this,
            com.doctoraak.doctoraakpatient.customView.DialogType.ERROR
        )
        sd.show()
        sd.setMessage(errorMessage)
        sd.setTitle(getString(R.string.error))
        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })
        sd.setOkClickListener(View.OnClickListener {
            sd.dismiss()
        }
        )
    }

    inner class ProfileClickHander() {
        fun onEditAddressClick() {
            showInputDialog(DialogType.Text, getString(R.string.enter_your_address))
        }

        fun onEditSecondPhoneNumber() {
            showInputDialog(DialogType.Number, getString(R.string.enter_phone))
        }

        fun onEditFullNameClick() {
            showInputDialog(DialogType.Text, getString(R.string.enter_your_name))
        }

        fun onEditBirthDateClick() {
            val dialogFragment = DatePikerFragment()
            dialogFragment.show(supportFragmentManager, "DatePikerdialog")
        }

        fun onEditPhotoClick() {
            if (isStoragePermissionGranted()) {
                selectImage(this@ProfileActivity)
            }
        }

        fun onEditGenderClick() {
            showGenderDialog()
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

    private fun isValidProfileDetails(): Boolean {
        when {
            binding.tvFulName.text.toString().trim().isEmpty() -> {
                showUnValidProfileDialog(getString(R.string.enter_your_name))
                binding.tvFulName.requestFocus()
                return false
            }
            binding.tvSecondPhone.text.toString().trim().isEmpty() -> {
                showUnValidProfileDialog(getString(R.string.enter_phone))
                binding.tvSecondPhone.requestFocus()
                return false
            }
            binding.tvSecondPhone.text.toString().trim().length != 11 -> {
                showUnValidProfileDialog(getString(R.string.must_be_11_digits))
                binding.tvSecondPhone.requestFocus()
                return false
            }
            else -> {
                return true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkFields() {
        if (binding.tvFulName.text.toString().trim().isEmpty()) {
            binding.fullNameText.text = "${resources.getText(R.string.name)} *"
            binding.ivEditFullName.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.red, null))
            binding.fullNameText.setTextColor(resources.getColor(R.color.red, null))
        } else {
            binding.fullNameText.text = resources.getText(R.string.name)
            binding.ivEditFullName.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorAccent, null))
            binding.fullNameText.setTextColor(resources.getColor(R.color.gray_1, null))
        }
        if (binding.tvSecondPhone.text.toString().trim().isEmpty()) {
            binding.tvPhoneSecondText.text = "${resources.getText(R.string.phone_number)} *"
            binding.ivEditPhone.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.red, null))
            binding.tvPhoneSecondText.setTextColor(resources.getColor(R.color.red, null))
        } else {
            binding.tvPhoneSecondText.text = resources.getText(R.string.phone_number)
            binding.ivEditPhone.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorAccent, null))
            binding.tvPhoneSecondText.setTextColor(resources.getColor(R.color.gray_1, null))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        checkFields()
    }


}
