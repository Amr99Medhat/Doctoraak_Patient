package com.doctoraak.doctoraakpatient.ui.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.*

class PaymentViewModel : ViewModel() {

    val paymentResponse: MutableLiveData<UserResponse>
            by lazy { MutableLiveData<UserResponse>() }
    val paymentAuthResponse: MutableLiveData<PaymentAuthResponse>
            by lazy { MutableLiveData<PaymentAuthResponse>() }

    val orderRegisterationResponse: MutableLiveData<OrderRegisterationResponse>
            by lazy { MutableLiveData<OrderRegisterationResponse>() }

    val paymentTokenResponse: MutableLiveData<CardPaymentKeyResposne>
            by lazy { MutableLiveData<CardPaymentKeyResposne>() }

    val payOrderResponse: MutableLiveData<PayOrderResponse>
            by lazy { MutableLiveData<PayOrderResponse>() }

    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun applyBuyCareience(id : Int) {

        PaymentRepository.applyBuyCareience( id,success = {
            paymentResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

    fun paymentAuth(apiKey : String) {
        PaymentRepository.paymentAuth( apiKey,success = {
            paymentAuthResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

    fun orderRegisteration(request: OrderRegisterationRequest) {
        PaymentRepository.orderRegisteration( request,success = {
            orderRegisterationResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

    fun getCardPaymentToken(request: CardPaymentKeyRequest) {
        PaymentRepository.getPaymentToken( request,success = {
            paymentTokenResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

    fun makePayOrder(request: PayOrderRequest) {
        PaymentRepository.makePayOrder( request,success = {
            payOrderResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }
}