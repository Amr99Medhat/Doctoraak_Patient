package com.doctoraak.doctoraakpatient.ui.radiologyOrder

import com.doctoraak.doctoraakpatient.model.RadiologyOrderRequest
import com.doctoraak.doctoraakpatient.model.RadiologyOrderResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiRadiology
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object RadiologyOrderRepository {

    fun createRadiologyOrder(request : RadiologyOrderRequest, success: (RadiologyOrderResponse)->Unit
                       , loading: ()->Unit
                       , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiRadiology.createRadiologyOrder(
            request,object : BaseResponseListener<RadiologyOrderResponse>
            {
                override fun onSuccess(model: RadiologyOrderResponse) {
                    success(model)
                }
                override fun onLoading()
                {
                    loading()
                }
                override fun onErrorMsg(errorMsg: String) {
                    errorMsg(errorMsg)
                }
                override fun onError(errorMsgId: Int)
                {
                    error(errorMsgId)
                }
            })
}