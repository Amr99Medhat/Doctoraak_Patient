package com.doctoraak.doctoraakpatient.ui.paymentDetails

import com.doctoraak.doctoraakpatient.model.PaymentDetailResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiIPayment
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object PaymentDeailsRepository {

    fun getPaymentDetails(success: (PaymentDetailResponse)->Unit, loading: ()->Unit
                            , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiIPayment.getPaymentDetails(object : BaseResponseListener<PaymentDetailResponse> {
            override fun onSuccess(model: PaymentDetailResponse) {
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