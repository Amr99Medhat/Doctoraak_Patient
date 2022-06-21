package com.doctoraak.doctoraakpatient.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.UpdatedProfileRequest
import com.doctoraak.doctoraakpatient.model.User
import com.doctoraak.doctoraakpatient.model.UserResponse

class ProfileViewModel : ViewModel() {

    val user: MutableLiveData<User>  by lazy { MutableLiveData(User()) }

    val updateProfileResponse: MutableLiveData<UserResponse>
            by lazy { MutableLiveData<UserResponse>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun updateProfile(request : UpdatedProfileRequest) {

        ProfileRepository.updateProfile(request, success = {
                updateProfileResponse.value = it; isLoading.value = false
            }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

}