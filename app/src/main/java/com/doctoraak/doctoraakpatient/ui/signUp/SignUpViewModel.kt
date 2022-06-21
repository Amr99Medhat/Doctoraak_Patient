package com.doctoraak.doctoraakpatient.ui.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.ResendSMSCodeResponse
import com.doctoraak.doctoraakpatient.model.User
import com.doctoraak.doctoraakpatient.model.UserResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.utils.SingleLiveEvent

class SignUpViewModel : ViewModel()
{
    val user: MutableLiveData<User>  by lazy { MutableLiveData(User()) }
    val userResponse: SingleLiveEvent<UserResponse> by lazy { SingleLiveEvent<UserResponse>() }
    val resendSmsReponse: SingleLiveEvent<ResendSMSCodeResponse> by lazy { SingleLiveEvent<ResendSMSCodeResponse>() }
    val mobileVerification: SingleLiveEvent<UserResponse> by lazy { SingleLiveEvent<UserResponse>() }
    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val errorMsg: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val errorInt: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }

    fun image(url: String)
    {
        user.value?.photo = url
        user.value = user.value

    }

    fun signUp()
    {

        SignUpRepository.register(user.value!!
            , success = {
                userResponse.postValue(it)
                isLoading.postValue(false)
            }
            , loading = { isLoading.postValue(true) }
            , errorMsg = { errorMsg.postValue(it); isLoading.postValue(false) }
            , error = {errorInt.postValue(it); isLoading.postValue(false)})
    }

    fun confirmPhone(smsCode: String)
    {
        if (SessionManager.isInMobileVerfi()){

            val id = SessionManager.getUserIDMobileVerfi()!!.toInt()

            SignUpRepository.mobileVerification(id.toLong(), smsCode
                , success = {
                    mobileVerification.postValue(it)
                    isLoading.postValue(false)
                }
                , loading = { isLoading.postValue(true) }
                , errorMsg = { errorMsg.postValue(it); isLoading.postValue(false) }
                , error = {errorInt.postValue(it); isLoading.postValue(false)})
        }else{

            SignUpRepository.mobileVerification(userResponse.value?.user?.id?.toLong()!!, smsCode
                , success = {
                    mobileVerification.postValue(it)
                    isLoading.postValue(false)
                }
                , loading = { isLoading.postValue(true) }
                , errorMsg = { errorMsg.postValue(it); isLoading.postValue(false) }
                , error = {errorInt.postValue(it); isLoading.postValue(false)})
        }

    }

    fun resendSmsCode(userId: Int) {
        SignUpRepository.resendSmsCode(userId
            , success = {
                resendSmsReponse.postValue(it)
                isLoading.postValue(false)
            }
            , loading = { isLoading.postValue(true) }
            , errorMsg = { errorMsg.postValue(it); isLoading.postValue(false) }
            , error = {errorInt.postValue(it); isLoading.postValue(false)})
    }

    fun termsAndConditions()
    {
        // todo handle Api For termsAndConditions.
    }

}