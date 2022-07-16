package com.doctoraak.doctoraakpatient.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.customView.DialogType
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import com.doctoraak.doctoraakpatient.databinding.ActivityForgetPasswordBinding
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.repository.remote.ApiCredential
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener
import com.doctoraak.doctoraakpatient.utils.*
import kotlinx.android.synthetic.main.dialog_sweet.*

class ForgetPasswordActivity : BaseActivity() {
    private val VIEW_GROUP_PHONE_KEY = "VIEW_GROUP_PHONE_KEY"
    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.lifecycleOwner = this

        binding.ivBack.setOnClickListener { finishAndBack() }
        binding.btnConfirmPhone.setOnClickListener { sendClick() }
        binding.tvResendSms.setOnClickListener { resendClick() }

        val phone = SessionManager.getPhoneForgetPasswordMode()
        if (!TextUtils.isEmpty(phone) && phone != null) {
            binding.viewGroupPhone.visibility = GONE
            binding.viewGroupCodePassword.visibility = VISIBLE
            binding.etPhone.setText(phone)
        }

        binding.loginMobVer.setOnClickListener {
            SessionManager.clearPhoneForgetPasswordMode()
            finish()
        }
    }

    private fun startLoading() {
        binding.btnConfirmPhone.isEnabled = false
        binding.loading.visibility = VISIBLE
    }

    private fun stopLoading() {
        binding.btnConfirmPhone.isEnabled = true
        binding.loading.visibility = GONE
    }

    private fun resendClick() {
        resendCode()
    }

    private fun resendCode() {
        val phone = binding.etPhone.text.toString()
        if (!phone.validatePhone(this, binding.etlPhone))
            return

        binding.etlPhone.error = null
        ApiCredential.forgetPasswordStep_1(phone, object : BaseResponseListener<BaseResponse> {
            override fun onSuccess(model: BaseResponse) {
                stopLoading()

            }

            override fun onLoading() {
                startLoading()
            }

            override fun onErrorMsg(errorMsg: String) {
                stopLoading()
                toast(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                stopLoading()
                toast(errorMsgId)
            }
        })
    }

    private fun sendClick() {
        if (binding.viewGroupPhone.visibility == VISIBLE){
            if (Utils.checkInternetConnection(this, binding.clForgetPassword))
                stepOne()
        }else{
            if (Utils.checkInternetConnection(this, binding.clForgetPassword))
                stepTwo()
        }

    }

    private fun stepOne() {

        val phone = binding.etPhone.text.toString()
        if (!phone.validatePhone(this, binding.etlPhone))
            return

        binding.etlPhone.error = null
        ApiCredential.forgetPasswordStep_1(phone, object : BaseResponseListener<BaseResponse> {
            override fun onSuccess(model: BaseResponse) {
                stopLoading()
                SessionManager.setPhoneForgetPasswordMode(binding.etPhone.text.toString())
                binding.viewGroupPhone.animateOutInX(binding.viewGroupCodePassword)
                binding.viewGroupPhone.visibility = GONE
                logd("fvvffv"  , "onSuccess")
            }

            override fun onLoading() {
                startLoading()
            }

            override fun onErrorMsg(errorMsg: String) {
                stopLoading()
                toast(errorMsg)
                logd("fvvffv"  , "errormsg")
            }

            override fun onError(errorMsgId: Int) {
                stopLoading()
                toast(errorMsgId)
                logd("fvvffv"  , "error")
            }
        })
    }

    private fun stepTwo() {

        val password = binding.etPassword.text.toString()
        val confirm_password = binding.etConfirmPassword.text.toString()
        val ver_code = binding.etCode.text.toString()

        if (password.validatePasswordAndConfirmPassword(
                this, binding.etlPassword, confirm_password
                , binding.etlConfirmPassword
            ) && ver_code.validateVerificationCode(this, binding.etlCode)
        )

            ApiCredential.forgetPasswordUpdatePasswordStep_2(binding.etPhone.text.toString()
                , ver_code, password, object : BaseResponseListener<BaseResponse> {
                    override fun onSuccess(model: BaseResponse) {
                        SessionManager.clearPhoneForgetPasswordMode()
                        stopLoading()
                        showSuccessDialog(getString(R.string.password_changed) , getString(R.string.done))

                    }

                    override fun onLoading() {
                        startLoading()
                    }

                    override fun onErrorMsg(errorMsg: String) {
                        stopLoading()
                        toast(errorMsg)
                    }

                    override fun onError(errorMsgId: Int) {
                        stopLoading()
                        toast(errorMsgId)
                    }
                })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(VIEW_GROUP_PHONE_KEY, binding.viewGroupPhone.visibility)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.let {
            val phone_visibility = it?.getInt(VIEW_GROUP_PHONE_KEY)
            if (phone_visibility == VISIBLE) {
                binding.viewGroupPhone.visibility = VISIBLE
                binding.viewGroupCodePassword.visibility = GONE
            } else {
                binding.viewGroupPhone.visibility = GONE
                binding.viewGroupCodePassword.visibility = VISIBLE
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        SessionManager.setPhoneForgetPasswordMode(binding.etPhone.text.toString())
    }

    override fun showSuccessDialog(msg : String, title : String){
        val sd = SweetDialog.newInstance(this, DialogType.SUCCESS)
        sd.show()
        sd.setMessage(msg)
        sd.setTitle(title)
        sd.btn_cancel.visibility = View.GONE
        sd.setCancelable(false)
        sd.setOkClickListener(View.OnClickListener {
            finish()
        }
        )
    }
}
