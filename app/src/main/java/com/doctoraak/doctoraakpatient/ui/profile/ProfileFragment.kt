package com.doctoraak.doctoraakpatient.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SpinnerAdapter
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
import com.doctoraak.doctoraakpatient.utils.Constants
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.Utils.Companion.showSnackbar
import kotlinx.android.synthetic.main.dialog_sweet.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private const val NAME_KEY = "NAME_KEY"
private const val PHOTO_KEY = "PHOTO_KEY"
private const val GENDER_KEY = "GENDER_KEY"

class ProfileFragment : BaseFragment() {
    private var name = ""
    private var photo = ""
    private var gender = ""
    private var patinet_name = ""
    private var phone2 = ""
    lateinit var currentPhotoPath: String
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mFragmentProfileBinding: FragmentProfileBinding

    companion object {

        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(NAME_KEY, name)
        outState.putString(PHOTO_KEY, photo)
        outState.putString(GENDER_KEY, gender)

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
        if (arguments!!.getString(Constants.MISSING_DATA) == Constants.MISSING_DATA) {

            showMissingDataDialog()

        }
        if (savedInstanceState == null) {
            initializeData()
            viewModel.user.value = Utils.getUser()
        } else {
            getCurrentStat(savedInstanceState)
        }
        mFragmentProfileBinding.lifecycleOwner = this
        mFragmentProfileBinding.userViewModel = viewModel
        mFragmentProfileBinding.clickHander = ProfileClickHander()
        initializeUI()

        mFragmentProfileBinding.btnSave.setOnClickListener {
            if (isValidProfileDetails()) {
                showWarringDialog()

            }
        }
        observeData()
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
    }

    private fun initializeUI() {
        Glide.with(this).load(photo)
            .placeholder(R.drawable.ic_face)
            .error((R.drawable.ic_face))
            .into(mFragmentProfileBinding.civImage)
        mFragmentProfileBinding.tvFullName.setText(viewModel.user.value!!.patient_name)
        mFragmentProfileBinding.tvEmail.setText(viewModel.user.value!!.email)
        mFragmentProfileBinding.btnBirthday.text = viewModel.user.value!!.birthdate



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
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { t ->
            if (t!!) {
                mFragmentProfileBinding.loading.visibility = View.VISIBLE
            } else {
                mFragmentProfileBinding.loading.visibility = View.INVISIBLE
            }
        }

        viewModel.errorMsg.observe(viewLifecycleOwner) { t ->
            if (!t.isNullOrEmpty()) {
                showSnackbar(mFragmentProfileBinding.clProfile, t)
                mFragmentProfileBinding.loading.visibility = View.INVISIBLE
            }
        }

        viewModel.errorInt.observe(viewLifecycleOwner) { t ->
            if (t!! != 0) {
                showSnackbar(mFragmentProfileBinding.clProfile, getString(t))
                mFragmentProfileBinding.loading.visibility = View.INVISIBLE
            }
        }
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
                            // showSaveButton("image loaded")
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
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
            sd.dismiss()
        }
        )
    }

    private fun showWarringDialog() {
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

            if (gender == "ذكر") {
                gender = "Male"
            } else if (gender == "أنثي") {
                gender = "Female"
            } else if (gender == resources.getString(R.string.gender)) {
                gender = ""
            }

            val request = UpdatedProfileRequest(
                Utils.getUserId().toString(),
                name,
                gender,
                mFragmentProfileBinding.btnBirthday.text.toString(),
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

    private fun showMissingDataDialog() {
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
            requireActivity().let {
                dialogFragment.show(requireActivity().supportFragmentManager,
                    "DatePikerdialog3030")
            }

            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.setFragmentResultListener("REQUEST_KEY",
                viewLifecycleOwner) { result_key, bundle ->
                if (result_key == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    if (date != null) {
                        mFragmentProfileBinding.btnBirthday.text = date
                    }
                }
            }

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


    private fun checkFields() {
        if (mFragmentProfileBinding.tvFullName.text.toString().trim().isEmpty()) {
            mFragmentProfileBinding.tilFullName.hintTextColor =
                (ColorStateList.valueOf(resources.getColor(R.color.red, null)))
            mFragmentProfileBinding.tvFullName.requestFocus()
            return
        } else {
            mFragmentProfileBinding.tilFullName.hintTextColor =
                (ColorStateList.valueOf(resources.getColor(R.color.colorPrimary_new, null)))
        }
        if (mFragmentProfileBinding.tvSecondPhone.text.toString().trim().isEmpty()) {
            mFragmentProfileBinding.tilSecondPhone.hintTextColor =
                (ColorStateList.valueOf(resources.getColor(R.color.red, null)))
            mFragmentProfileBinding.tvSecondPhone.requestFocus()
        } else {
            mFragmentProfileBinding.tilSecondPhone.hintTextColor =
                (ColorStateList.valueOf(resources.getColor(R.color.colorPrimary_new, null)))
        }
    }

    override fun onResume() {
        super.onResume()
        checkFields()
    }

}