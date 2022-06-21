package com.doctoraak.doctoraakpatient.ui.lap

import com.doctoraak.doctoraakpatient.model.LabFilter
import com.doctoraak.doctoraakpatient.model.LabResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiLab
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object LapRepository {


    fun filterLaps(filter : LabFilter, pageNum: Int, success: (LabResponse) -> Unit
                   , loading: () -> Unit
                   , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiLab.filterLabs( filter, pageNum, object : BaseResponseListener<LabResponse> {
                override fun onSuccess(model: LabResponse) {
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