package com.doctoraak.doctoraakpatient.ui.bookDoctorDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.ClinicOrderResponse

class BookDoctorDetailsViewModel : ViewModel() {

    val clinicOrderResponse: MutableLiveData<ClinicOrderResponse>
            by lazy { MutableLiveData<ClinicOrderResponse>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun createOrderClinic(
        patientId: Int,
        clinic_id: Int,
        date: String,
        type: Int
        , notes: String
        , api_token: String , part : Int
    ) {

        BookDoctorDetailsRepository.createOrderClinic(
            patientId, clinic_id,
            date, type, notes, api_token,part , success = {
                clinicOrderResponse.value = it; isLoading.value = false
            }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }


}