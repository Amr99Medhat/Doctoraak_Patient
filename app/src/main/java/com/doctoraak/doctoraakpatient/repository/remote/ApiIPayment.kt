package com.doctoraak.doctoraakpatient.repository.remote


import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.utils.start
import retrofit2.Call

object ApiIPayment {

    fun getPaymentDetails(callback: BaseResponseListener<PaymentDetailResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<PaymentDetailResponse> = service.getPaymentDetails()
        call.start(callback)
    }

    fun applyBuyCareience(id: Int, callback: BaseResponseListener<UserResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<UserResponse> = service.applayBuyCareience(id)
        call.start(callback)
    }

    fun paymentAuth(apiKey: String, callback: BaseResponseListener<PaymentAuthResponse>) {
        val service = ApiConfigure.paymentRetrofit.create(ApiEndPoint::class.java)
        val body = PaymentAuthRequest(apiKey)
        val call: Call<PaymentAuthResponse> = service.paymentAuth(body)
        call.start(callback)
    }

    fun orderRegisteration(request: OrderRegisterationRequest,
                           callback: BaseResponseListener<OrderRegisterationResponse>) {
        val service = ApiConfigure.paymentRetrofit.create(ApiEndPoint::class.java)
        val call: Call<OrderRegisterationResponse> = service.orderRegisteration(request)
        call.start(callback)
    }

    fun getPaymentToken(request: CardPaymentKeyRequest,
                        callback: BaseResponseListener<CardPaymentKeyResposne>) {
        val service = ApiConfigure.paymentRetrofit.create(ApiEndPoint::class.java)
        val call: Call<CardPaymentKeyResposne> = service.getPaymentToken(request)
        call.start(callback)
    }

    fun makePayOrder(request: PayOrderRequest, callback: BaseResponseListener<PayOrderResponse>) {
        val service = ApiConfigure.paymentRetrofit.create(ApiEndPoint::class.java)
        val call: Call<PayOrderResponse> = service.makePayRequest(request)
        call.start(callback)
    }

}