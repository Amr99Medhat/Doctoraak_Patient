package com.doctoraak.doctoraakpatient.ui.bookDoctorDetail

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.RadioButton
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.customView.DialogType
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import com.doctoraak.doctoraakpatient.databinding.ActivityBookDoctorDetailsBinding
import com.doctoraak.doctoraakpatient.model.ClinicOrderResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.DatePikerFragment
import com.doctoraak.doctoraakpatient.ui.myOrders.MyOrdersActivity
import com.doctoraak.doctoraakpatient.utils.*
import kotlinx.android.synthetic.main.dialog_sweet.*

class BookDoctorDetailsActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date = "$year-${(month + 1)}-$dayOfMonth"
        binding.etDate.setText(date)
    }

    private val viewModel by lazy { ViewModelProvider(this).get(BookDoctorDetailsViewModel::class.java) }
    private lateinit var binding: ActivityBookDoctorDetailsBinding

    private var shift = -1
    private var type = -1
    private var date = ""
    private var notes = ""
    private var clinicId = -1

    private val DATE_KEY = "DATE_KEY"
    private val TYPE_KEY = "TYPE_KEY"
    private val SHIFT_KEY = "SHIFT_KEY"
    private val NAME_VISIBILTY_KEY = "NAME_VISIBILTY_KEY"
    private val PHONE_VISIBILTY_KEY = "PHONE_VISIBILTY_KEY"
    private val AGE_VISIBILTY_KEY = "AGE_VISIBILTY_KEY"
    private val GENDER_VISIBILTY_KEY = "GENDER_VISIBILTY_KEY"


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TYPE_KEY , type)
        outState.putInt(SHIFT_KEY , shift)
        outState.putString(DATE_KEY , date)
        outState.putInt(NAME_VISIBILTY_KEY , binding.etlName.visibility)
        outState.putInt(PHONE_VISIBILTY_KEY , binding.etlPhone.visibility)
        outState.putInt(AGE_VISIBILTY_KEY , binding.etlAge.visibility)
        outState.putInt(GENDER_VISIBILTY_KEY , binding.genderGroup.visibility)

    }

    private fun getState(savedInstanceState: Bundle) {

        type = savedInstanceState.getInt(TYPE_KEY , -1)
        shift = savedInstanceState.getInt(SHIFT_KEY , -1)
        date = savedInstanceState.getString(DATE_KEY , "")

        binding.etlName.visibility = savedInstanceState.getInt(NAME_VISIBILTY_KEY)
        binding.etlPhone.visibility = savedInstanceState.getInt(PHONE_VISIBILTY_KEY)
        binding.etlAge.visibility = savedInstanceState.getInt(AGE_VISIBILTY_KEY)
        binding.genderGroup.visibility = savedInstanceState.getInt(GENDER_VISIBILTY_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_book_doctor_details
        )
        if (savedInstanceState != null){
            getState(savedInstanceState)
        }

        binding.lifecycleOwner = this

        clinicId = intent.getIntExtra(getString(R.string.clinic_id_key), -1)
        val bookTypes = arrayListOf<String>(
            getString(R.string.reservation), getString(R.string.consultion), getString(R.string.continuee))

        val shifts = arrayListOf<String>(
            getString(R.string.shift_1), getString(R.string.shift_2))

        setupAutoCompleteTextView(binding.spType, bookTypes)
        setupAutoCompleteTextView(binding.spShift, shifts)

        binding.btnBook.setOnClickListener {
                if (binding.etlName.visibility == View.GONE) {
                    sendDataForUser()
                } else {
                    sendDataForOther()
                }
        }

        binding.etDate.setOnClickListener {
            val dialogFragment = DatePikerFragment(false)
            dialogFragment.show(supportFragmentManager, "DatePiker")
        }

        setListenerForAutoCompleteTextView()
        setListenerForAutoCompleteTextViewShifts()
        setListenerForRadioButtons()
        observeData()
        initData()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun initData()
    {
        intent?.getIntExtra(getString(R.string.book_shift_key), -1)?.let {
            if (it == 1)
            {
                shift = it
                binding.spShift.setText(getString(R.string.shift_1))
            }
            else if (it == 2)
            {
                shift = it
                binding.spShift.setText(getString(R.string.shift_2))
            }
        }

        intent?.getStringExtra(getString(R.string.book_date_key))?.let {
            Log.d("saif2", "date= $it")
            date = it
            binding.etDate.setText(date)
        }
    }

    private fun setListenerForRadioButtons() {
        val motion  = findViewById<MotionLayout>(R.id.motion_layout)
        binding.tvBookMe.setOnClickListener {

            if (binding.etlName.visibility != View.GONE) {
                binding.etlName.visibility = View.GONE
                binding.etlPhone.visibility = View.GONE
                binding.etlAge.visibility = View.GONE
                binding.genderGroup.visibility = View.GONE
                motion.setTransition(R.id.start2 , R.id.end2)
                motion.transitionToEnd()
            }
        }

        binding.tvBookForAnother.setOnClickListener {
            if (binding.etlName.visibility == View.GONE) {
                binding.etlName.visibility = View.VISIBLE
                binding.etlPhone.visibility = View.VISIBLE
                binding.etlAge.visibility = View.VISIBLE
                binding.genderGroup.visibility = View.VISIBLE
                motion.setTransition(R.id.start , R.id.end)
                motion.transitionToEnd()
            }
        }
    }

    private fun setListenerForAutoCompleteTextViewShifts() {
        binding.spShift.setOnItemClickListener { parent, view, position, id ->
            shift = (position + 1)
        }
    }

    private fun setListenerForAutoCompleteTextView() {
        binding.spType.setOnItemClickListener { parent, view, position, id ->
            type = (position + 1)
        }
    }

    private fun sendDataForOther() {
        val selectedId = binding.genderGroup.checkedRadioButtonId

        val radioButton = findViewById<View>(selectedId) as RadioButton
        val isValidateName = binding.etName.text.toString().validateName(this, binding.etlName)
        val isValidateAge = binding.etAge.text.toString().validateAge(this, binding.etlAge)
        val isValidatePhone = binding.etPhone.text.toString().validatePhone(this, binding.etlPhone)
        val isValidateDate = date.validateDate(this, binding.etlDate)
        val isValidatetype = type.validateBookType(this, binding.splType)
        val isValidateShift = shift.validateShift(this, binding.splShift)


        if (isValidateName && isValidateAge && isValidatePhone&&isValidateDate && isValidatetype && isValidateShift) {
            notes = "${binding.etName.text},${binding.etPhone.text}" +
                    ",${binding.etAge.text},${radioButton.text}"
            if (Utils.checkInternetConnection(this, binding.clBookDoctotDetail)) {
                if (SessionManager.isLogIn()) {
                    viewModel.createOrderClinic(Utils.getUserId(), clinicId, date,
                        type, notes, Utils.getApiToken(), shift
                    )
                } else {
                    showLoginFirstDialog(getString(R.string.login_first))
                }
            }
        }

    }

    private fun sendDataForUser()
    {
        val isValidateDate = date.validateDate(this, binding.etlDate)
        val isValidatetype = type.validateBookType(this, binding.splType)
        val isValidateShift = shift.validateShift(this, binding.splShift)

        if (isValidateDate && isValidatetype && isValidateShift) {
            if (Utils.checkInternetConnection(this, binding.clBookDoctotDetail)) {
                if (SessionManager.isLogIn()) {
                    viewModel.createOrderClinic(Utils.getUserId(), clinicId, date, type, "", Utils.getApiToken(), shift)
                } else {
                    showLoginFirstDialog(getString(R.string.login_first))
                }
            }
        }
    }

    private fun observeData() {

        viewModel.clinicOrderResponse.observe(this, object : Observer<ClinicOrderResponse> {
            override fun onChanged(t: ClinicOrderResponse?) {

                showSuccessDialog(t!!.getMsg(), getString(R.string.order_has_been_send))
                binding.loading!!.visibility = View.INVISIBLE
                enableButton(binding.btnBook)

            }
        })
        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    binding.loading!!.visibility = View.VISIBLE
                    disableButton(binding.btnBook)
                } else {
                    enableButton(binding.btnBook)
                    binding.loading!!.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clBookDoctotDetail , t)
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.btnBook)
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!! != 0) {
                    showSnackbar(binding.clBookDoctotDetail , getString(t))
                    binding.loading!!.visibility = View.INVISIBLE
                    enableButton(binding.btnBook)
                }
            }
        })

    }

    override fun showSuccessDialog(msg: String, title: String) {
        val sd = SweetDialog.newInstance(this, DialogType.SUCCESS)
        sd.show()
        sd.setMessage(msg)
        sd.setTitle(title)
        sd.btn_cancel.visibility = View.GONE
        sd.setCancelable(false)
        sd.setOkClickListener(View.OnClickListener {
            val intent = Intent(this, MyOrdersActivity::class.java)
            intent.putExtra(getString(R.string.isBackFromBookDoctorKey), true)
            startActivity(intent)
            finish()
        }
        )
    }

}
