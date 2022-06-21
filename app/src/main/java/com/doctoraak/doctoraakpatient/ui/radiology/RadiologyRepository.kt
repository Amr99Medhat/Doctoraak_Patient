package com.doctoraak.doctoraakpatient.ui.radiology

import com.doctoraak.doctoraakpatient.model.RadiologyFilter
import com.doctoraak.doctoraakpatient.model.RadiologyResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiRadiology
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object RadiologyRepository {

    fun filterRadiologies(filter : RadiologyFilter, pageNum: Int, success: (RadiologyResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiRadiology.filterRadiologies(filter, pageNum, object : BaseResponseListener<RadiologyResponse> {
                override fun onSuccess(model: RadiologyResponse) {
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