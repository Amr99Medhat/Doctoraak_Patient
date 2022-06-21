package com.doctoraak.doctoraakpatient.ui.favouriteDoctor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.ClinicsResponse
import com.doctoraak.doctoraakpatient.model.FavDoctorResponse

class FavoriteDoctorViewModel : ViewModel() {

    //get all fav doctors
    val favResposne: MutableLiveData<ClinicsResponse>
            by lazy { MutableLiveData<ClinicsResponse>() }

    //fav and unfav doctor
    val favDoctorResposne: MutableLiveData<FavDoctorResponse>
            by lazy { MutableLiveData<FavDoctorResponse>() }

    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun getFavouriteDoctors(patientId: Int , apiToken: String)
    {
        FavoriteDoctorRepository.getFavouriteDoctors(patientId , apiToken,success = {
            favResposne.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }

    fun favDoctor(patientId: Int ,doctorId : Int , apiToken: String)
    {
        FavoriteDoctorRepository.favDoctor(patientId,doctorId , apiToken,success = {
            favDoctorResposne.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }

}