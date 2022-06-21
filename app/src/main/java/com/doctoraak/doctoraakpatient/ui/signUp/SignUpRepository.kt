package com.doctoraak.doctoraakpatient.ui.signUp

import android.util.Log
import com.doctoraak.doctoraakpatient.model.ResendSMSCodeResponse
import com.doctoraak.doctoraakpatient.model.User
import com.doctoraak.doctoraakpatient.model.UserResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiCredential
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object SignUpRepository
{
    fun register(user: User, success: (UserResponse)->Unit, loading: ()->Unit
                 , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiCredential.register(user, object : BaseResponseListener<UserResponse>
        {
            override fun onSuccess(model: UserResponse)
            {
                Log.d("saif", "onsuccess")
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                Log.d("saif", "onErrorMsg")
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                Log.d("saif", "onError")
                error(errorMsgId)
            }
        })

    fun  mobileVerification(user_id: Long, smsCode: String, success: (UserResponse)->Unit, loading: ()->Unit
                            , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiCredential.activeAccount(user_id, smsCode, object : BaseResponseListener<UserResponse> {
            override fun onSuccess(model: UserResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })

    fun  resendSmsCode(user_id: Int, success: (ResendSMSCodeResponse)->Unit, loading: ()->Unit
                            , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiCredential.resendSmsCode(user_id, object : BaseResponseListener<ResendSMSCodeResponse> {
            override fun onSuccess(model: ResendSMSCodeResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })
}