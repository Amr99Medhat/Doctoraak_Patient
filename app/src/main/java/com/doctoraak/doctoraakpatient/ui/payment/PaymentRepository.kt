package com.doctoraak.doctoraakpatient.ui.payment

import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.remote.ApiIPayment
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object PaymentRepository {

    fun applyBuyCareience(id : Int ,success: (UserResponse)->Unit, loading: ()->Unit
                            , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiIPayment.applyBuyCareience( id, object : BaseResponseListener<UserResponse> {
            override fun onSuccess(model: UserResponse) {
                success(model)
            }

            override fun onLoading() {
                loading()
             }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })

    fun paymentAuth(apiKey : String, success: (PaymentAuthResponse)->Unit, loading: ()->Unit
                    , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiIPayment.paymentAuth( apiKey, object : BaseResponseListener<PaymentAuthResponse> {
            override fun onSuccess(model: PaymentAuthResponse) {
                success(model)
            }

            override fun onLoading() {
                loading()
            }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })

    fun orderRegisteration(request: OrderRegisterationRequest, success: (OrderRegisterationResponse)->Unit, loading: ()->Unit
                    , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiIPayment.orderRegisteration( request, object : BaseResponseListener<OrderRegisterationResponse> {
            override fun onSuccess(model: OrderRegisterationResponse) {
                success(model)
            }

            override fun onLoading() {
                loading()
            }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })

    fun getPaymentToken(request: CardPaymentKeyRequest, success: (CardPaymentKeyResposne)->Unit, loading: ()->Unit
                           , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiIPayment.getPaymentToken( request, object : BaseResponseListener<CardPaymentKeyResposne> {
            override fun onSuccess(model: CardPaymentKeyResposne) {
                success(model)
            }

            override fun onLoading() {
                loading()
            }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })

    fun makePayOrder(request: PayOrderRequest, success: (PayOrderResponse)->Unit, loading: ()->Unit
                        , errorMsg: (String)-> Unit, error: (Int)-> Unit) =
        ApiIPayment.makePayOrder( request, object : BaseResponseListener<PayOrderResponse> {
            override fun onSuccess(model: PayOrderResponse) {
                success(model)
            }

            override fun onLoading() {
                loading()
            }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })
}