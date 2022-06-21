package com.doctoraak.doctoraakpatient.ui.findServcie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.RatingResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.ReservationsResponse

class FindServiceViewModel : ViewModel() {

    val resevationsReponse: MutableLiveData<ReservationsResponse>
            by lazy { MutableLiveData<ReservationsResponse>() }
    val rateDoctorResposne: MutableLiveData<RatingResponse>
            by lazy { MutableLiveData<RatingResponse>() }

    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun getReservations(
        patientId: Int,
        apiToken: String
    ) {

        FindServiceRepository.getReservations(patientId, apiToken, success = {
            resevationsReponse.value = it; isLoading.value = false
            }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

    fun rateDoctor(patientId: Int,doctorId: Int ,rate : Int, apiToken: String , type : String)
    {
        FindServiceRepository.rateDoctor(patientId,doctorId ,rate, apiToken,type,success = {
            rateDoctorResposne.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false;errorMsg.value = it}
            , error = {isLoading.value = false; errorInt.value = it} )
    }
}