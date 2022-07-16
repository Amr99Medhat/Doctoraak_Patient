package com.doctoraak.doctoraakpatient.ui.profile

import android.Manifest
import android.annotation.SuppressLint
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import com.doctoraak.doctoraakpatient.databinding.FragmentProfileBinding
import com.doctoraak.doctoraakpatient.model.UpdatedProfileRequest
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseFragment
import com.doctoraak.doctoraakpatient.ui.DatePikerFragment
import com.doctoraak.doctoraakpatient.ui.main.MainActivity
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.Utils.Companion.showSnackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_sweet.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private val NAME_KEY = "NAME_KEY"
private val PHOTO_KEY = "PHOTO_KEY"
private val GENDER_KEY = "GENDER_KEY"
private val BUTTON_VISIBILITY_KEY = "BUTTON_VISIBILITY_KEY"

class ProfileFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {
    private var name = ""
    private var photo = ""
    private var gender = ""
    private var patinet_name = ""
    private var phone2 = ""
    lateinit var currentPhotoPath: String
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mFragmentProfileBinding: FragmentProfileBinding

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(NAME_KEY, name)
        outState.putString(PHOTO_KEY, photo)
        outState.putString(GENDER_KEY, gender)
        outState.putInt(BUTTON_VISIBILITY_KEY, mFragmentProfileBinding.btnSave.visibility)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mFragmentProfileBinding = FragmentProfileBinding.inflate(inflater)

        return mFragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val missing = activity?.intent!!.getBooleanExtra(Constants.MISSING_DATA, false)
//        if (missing) {
//            showMissingDataDialog()
//            Toast.makeText(requireContext(), "showMissingDataDialog", Toast.LENGTH_SHORT).show()
//        }

        if (savedInstanceState == null) {
            initializeData()
            viewModel.user.value = Utils.getUser()
        } else {
            getCurrentStat(savedInstanceState)
        }
        mFragmentProfileBinding.lifecycleOwner = this
        mFragmentProfileBinding.userViewModel = viewModel
        mFragmentProfileBinding.clickHander = ProfileClickHander()
        iniatializeUI()

        mFragmentProfileBinding.btnSave.setOnClickListener {
            if (isValidProfileDetails()) {
                showWrringDialog()

            }
        }
        observeData()
        //setListenerToInfoButtons()

        val logo = requireActivity().findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }

    }


    private fun initializeData() {
        val user = Utils.getUser()
        name = Utils.getUserName()
        gender = user.gender
        photo = user.photo
        patinet_name = user.patient_name
        phone2 = user.phone2
    }

    private fun getCurrentStat(savedInstanceState: Bundle) {
        name = savedInstanceState.getString(NAME_KEY, "")
        photo = savedInstanceState.getString(PHOTO_KEY, "")
        gender = savedInstanceState.getString(GENDER_KEY, "")
        mFragmentProfileBinding.btnSave.visibility =
            savedInstanceState.getInt(BUTTON_VISIBILITY_KEY)
    }

    private fun iniatializeUI() {
        Glide.with(this).load(photo)
            .placeholder(R.drawable.ic_face)
            .error((R.drawable.ic_face))
            .into(mFragmentProfileBinding.civImage)

        mFragmentProfileBinding.tvFullName.setText(Utils.getUserName())
        mFragmentProfileBinding.tvEmail.setText(viewModel.user.value!!.email)


        if (gender == "Male" || gender == "ذكر") {
            mFragmentProfileBinding.sprGender.adapter = activity?.applicationContext?.let {
                ArrayAdapter(it,
                    R.layout.item_spinner,
                    arrayListOf(resources.getString(R.string.male),
                        resources.getString(R.string.female)))
            } as SpinnerAdapter
        } else if (gender == "Female" || gender == "أنثي") {
            mFragmentProfileBinding.sprGender.adapter = activity?.applicationContext?.let {
                ArrayAdapter(it,
                    R.layout.item_spinner,
                    arrayListOf(resources.getString(R.string.female),
                        resources.getString(R.string.male)))
            } as SpinnerAdapter
        } else {
            mFragmentProfileBinding.sprGender.adapter = activity?.applicationContext?.let {
                ArrayAdapter(it,
                    R.layout.item_spinner,
                    arrayListOf(resources.getString(R.string.gender),
                        resources.getString(R.string.male),
                        resources.getString(R.string.female)))
            } as SpinnerAdapter
        }
    }


    private fun observeData() {

        viewModel.updateProfileResponse.observe(viewLifecycleOwner
        ) { t ->
            SessionManager.logIn(t!!.user!!)
            showSuccessDialog(
                getString(R.string.profile_updated_successfulty),
                getString(R.string.done)
            )
            mFragmentProfileBinding.loading.visibility = View.INVISIBLE
            enableButton(mFragmentProfileBinding.btnSave)
            setVisiblityGone(mFragmentProfileBinding.btnSave)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { t ->
            if (t!!) {
                mFragmentProfileBinding.loading.visibility = View.VISIBLE
                disableButton(mFragmentProfileBinding.btnSave)
            } else {
                mFragmentProfileBinding.loading.visibility = View.INVISIBLE
                enableButton(mFragmentProfileBinding.btnSave)
            }
        }

        viewModel.errorMsg.observe(viewLifecycleOwner) { t ->
            if (!t.isNullOrEmpty()) {
                showSnackbar(mFragmentProfileBinding.clProfile, t)
                mFragmentProfileBinding.loading.visibility = View.INVISIBLE
                enableButton(mFragmentProfileBinding.btnSave)
            }
        }

        viewModel.errorInt.observe(viewLifecycleOwner) { t ->
            if (t!! != 0) {
                showSnackbar(mFragmentProfileBinding.clProfile, getString(t))
                mFragmentProfileBinding.loading.visibility = View.INVISIBLE
                enableButton(mFragmentProfileBinding.btnSave)
            }
        }
    }

//    @SuppressLint("SetTextI18n")
//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        mFragmentProfileBinding.btnBirthday.text = "$year-${(month + 1)}-$dayOfMonth"
//        Log.d("date","$year-${(month + 1)}-$dayOfMonth")
//        showSaveButton("$year-${(month + 1)}-$dayOfMonth")
//    }

    private fun showSaveButton(text: String) {
        if (mFragmentProfileBinding.btnSave.visibility != View.VISIBLE && text.isNotEmpty())
            mFragmentProfileBinding.btnSave.visibility = View.VISIBLE
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
                    val photoURI = activity?.let {
                        FileProvider.getUriForFile(
                            it,
                            "com.doctoraak.doctoraakpatient.fileprovider", photoFile
                        )
                    }
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


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, activity?.intent)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK) {

                    val file = File(currentPhotoPath);
                    val bitmap: Bitmap
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media
                            .getBitmap(activity?.contentResolver, Uri.fromFile(file))
                    } else {
                        val source =
                            activity?.let {
                                ImageDecoder.createSource(it.contentResolver,
                                    Uri.fromFile(file))
                            }
                        bitmap = source?.let { ImageDecoder.decodeBitmap(it) }!!
                    }
                    mFragmentProfileBinding.civImage.setImageBitmap(bitmap)
                    showSaveButton("image loaded")
                    photo = currentPhotoPath

                }
                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor = activity?.contentResolver!!.query(
                            selectedImage,
                            filePathColumn, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()

                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            photo = cursor.getString(columnIndex)
                            mFragmentProfileBinding.civImage.setImageURI(selectedImage)
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
            if (checkSelfPermission(requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else {

                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                }
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
            activity?.let { selectImage(it) }
        }
    }

    override fun showSuccessDialog(msg: String, title: String) {
        val sd = activity?.let {
            SweetDialog.newInstance(
                it,
                com.doctoraak.doctoraakpatient.customView.DialogType.SUCCESS
            )
        }
        sd!!.show()
        sd.setMessage(msg)
        sd.setTitle(title)
        sd.btn_cancel.visibility = View.GONE
        sd.setCancelable(false)
        sd.setOkClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            //Toast.makeText(requireContext(),"Good",Toast.LENGTH_SHORT).show()

            sd.dismiss()
        }
        )
    }

    fun showWrringDialog() {
        val sd = activity?.let {
            SweetDialog.newInstance(
                it,
                com.doctoraak.doctoraakpatient.customView.DialogType.WARNING
            )
        }
        sd!!.show()
        sd.setMessage(getString(R.string.are_sure_you_to_update_profile))
        sd.setTitle(getString(R.string.warning))
        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })
        sd.setOkClickListener(View.OnClickListener {

            patinet_name = mFragmentProfileBinding.tvFullName.text.toString()
            phone2 = mFragmentProfileBinding.tvSecondPhone.text.toString()
            gender = mFragmentProfileBinding.sprGender.selectedItem.toString()

            if (gender == "ذكر"){
                gender = "Male"
            }else if (gender == "أنثي"){
                gender = "Female"
            }else if (gender == resources.getString(R.string.gender)){
                gender = ""
            }

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

            if (activity?.let { it1 ->
                    Utils.checkInternetConnection(it1,
                        mFragmentProfileBinding.clProfile)
                } == true) {
                viewModel.updateProfile(request)
            }
            sd.dismiss()
        }
        )
    }

    fun showMissingDataDialog() {
        val sd = activity?.let {
            SweetDialog.newInstance(
                it,
                com.doctoraak.doctoraakpatient.customView.DialogType.ERROR
            )
        }
        sd!!.show()
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
        val sd = activity?.let {
            SweetDialog.newInstance(
                it,
                com.doctoraak.doctoraakpatient.customView.DialogType.ERROR
            )
        }
        sd!!.show()
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

        fun onEditBirthDateClick() {
            val dialogFragment = DatePikerFragment(true)
            requireActivity().let { dialogFragment.show(it.supportFragmentManager, "DatePikerdialog3030") }
        }

        fun onEditPhotoClick() {
            if (isStoragePermissionGranted()) {
                activity?.let { selectImage(it) }
            }
        }

    }

    private fun isValidProfileDetails(): Boolean {
        when {
            mFragmentProfileBinding.tvFullName.text.toString().trim().isEmpty() -> {
                showUnValidProfileDialog(getString(R.string.enter_your_name))
                mFragmentProfileBinding.tvFullName.requestFocus()
                return false
            }
            mFragmentProfileBinding.tvSecondPhone.text.toString().trim().isEmpty() -> {
                showUnValidProfileDialog(getString(R.string.enter_phone))
                mFragmentProfileBinding.tvSecondPhone.requestFocus()
                return false
            }
            mFragmentProfileBinding.tvSecondPhone.text.toString().trim().length != 11 -> {
                showUnValidProfileDialog(getString(R.string.must_be_11_digits))
                mFragmentProfileBinding.tvSecondPhone.requestFocus()
                return false
            }
            else -> {
                return true
            }
        }
    }

//    @SuppressLint("SetTextI18n")
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun checkFields() {
//        if (binding.tvFulName.text.toString().trim().isEmpty()) {
//            binding.fullNameText.text = "${resources.getText(R.string.name)} *"
//            binding.ivEditFullName.backgroundTintList =
//                ColorStateList.valueOf(resources.getColor(R.color.red, null))
//            binding.fullNameText.setTextColor(resources.getColor(R.color.red, null))
//        } else {
//            binding.fullNameText.text = resources.getText(R.string.name)
//            binding.ivEditFullName.backgroundTintList =
//                ColorStateList.valueOf(resources.getColor(R.color.colorAccent, null))
//            binding.fullNameText.setTextColor(resources.getColor(R.color.gray_1, null))
//        }
//        if (binding.tvSecondPhone.text.toString().trim().isEmpty()) {
//            binding.tvPhoneSecondText.text = "${resources.getText(R.string.phone_number)} *"
//            binding.ivEditPhone.backgroundTintList =
//                ColorStateList.valueOf(resources.getColor(R.color.red, null))
//            binding.tvPhoneSecondText.setTextColor(resources.getColor(R.color.red, null))
//        } else {
//            binding.tvPhoneSecondText.text = resources.getText(R.string.phone_number)
//            binding.ivEditPhone.backgroundTintList =
//                ColorStateList.valueOf(resources.getColor(R.color.colorAccent, null))
//            binding.tvPhoneSecondText.setTextColor(resources.getColor(R.color.gray_1, null))
//        }
//    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }

//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        Log.d("date","$year-${(month + 1)}-$dayOfMonth")
//        val date = "$year-${(month + 1)}-$dayOfMonth"
//        Toast.makeText(requireContext(),date,Toast.LENGTH_SHORT).show()
//    }


}