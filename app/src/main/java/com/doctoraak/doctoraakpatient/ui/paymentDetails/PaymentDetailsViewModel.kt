package com.doctoraak.doctoraakpatient.ui.paymentDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.PaymentDetailResponse

class PaymentDetailsViewModel : ViewModel() {

    val paymentDetailsResponse: MutableLiveData<PaymentDetailResponse>
            by lazy { MutableLiveData<PaymentDetailResponse>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun getPaymentDetails() {

        PaymentDeailsRepository.getPaymentDetails(success = {
            paymentDetailsResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }
}