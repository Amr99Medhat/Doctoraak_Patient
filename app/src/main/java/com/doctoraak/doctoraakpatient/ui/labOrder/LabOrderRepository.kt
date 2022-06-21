package com.doctoraak.doctoraakpatient.ui.labOrder

import com.doctoraak.doctoraakpatient.model.LabOrderRequest
import com.doctoraak.doctoraakpatient.model.LabOrderResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiLab
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object LabOrderRepository {

    fun createLabOrder(request : LabOrderRequest,success: (LabOrderResponse)->Unit
                            , loading: ()->Unit
                            , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiLab.createLabOrder(
            request,object : BaseResponseListener<LabOrderResponse>
            {
                override fun onSuccess(model: LabOrderResponse) {
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