package com.doctoraak.doctoraakpatient.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.ClinicsResponse

class MainViewModel : ViewModel() {

    //get all fav doctors
    val favResposne: MutableLiveData<ClinicsResponse>
            by lazy { MutableLiveData<ClinicsResponse>() }

    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun getFavouriteDoctors(patientId: Int , apiToken: String) {
        MainRepository.getFavouriteDoctors(patientId , apiToken,success = {
            favResposne.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }


}