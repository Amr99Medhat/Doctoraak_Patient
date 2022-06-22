package com.doctoraak.doctoraakpatient.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivitySignInBinding
import com.doctoraak.doctoraakpatient.model.UserResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.repository.remote.ApiCredential
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener
import com.doctoraak.doctoraakpatient.ui.main.MainActivity
import com.doctoraak.doctoraakpatient.ui.signUp.SignUpActivity
import com.doctoraak.doctoraakpatient.utils.validateEmpty
import com.doctoraak.doctoraakpatient.utils.validatePassword
import com.doctoraak.doctoraakpatient.utils.validatePhone

class SignInActivity : BaseActivity(), BaseResponseListener<UserResponse>
{
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        binding.lifecycleOwner = this

        binding.tvForgetPassword.setOnClickListener { forgetPasswordClick() }
        binding.tvSignUp.setOnClickListener { SignUpClick() }
        binding.btnLogin.setOnClickListener { logInClick() }
    }

    private fun startLoading() {
        binding.btnLogin.isEnabled = false
        binding.loading.visibility = VISIBLE
    }

    private fun stopLoading() {
        binding.btnLogin.isEnabled = true
        binding.loading.visibility = GONE
    }

    private fun forgetPasswordClick() {
        startActivity(Intent(this, ForgetPasswordActivity::class.java))
    }

    private fun SignUpClick() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun logInClick()
    {
        val phone = binding.etPhone.text.toString()
        val password = binding.etPassword.text.toString()

        val isPhone = phone.validateEmpty(this, binding.etlPhone)
        val isPassword = password.validatePassword(this, binding.etlPassword)

        if (isPassword && isPhone)
            ApiCredential.logIn(phone, password, this)
    }


    override fun onSuccess(model: UserResponse) {
        stopLoading()
        model.let {
            SessionManager.removeMobileVerfiMode()
            SessionManager.logIn(it.user!!)
            finish()
        }
    }

    override fun onLoading() {
        startLoading()
    }

    override fun onErrorMsg(errorMsg: String) {
        stopLoading()
        toast(errorMsg.trim())

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onError(errorMsgId: Int) {
        stopLoading()
        if (errorMsgId != 0)
        toast(errorMsgId)
    }

}