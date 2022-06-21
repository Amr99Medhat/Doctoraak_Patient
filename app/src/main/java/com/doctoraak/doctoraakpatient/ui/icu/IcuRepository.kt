package com.doctoraak.doctoraakpatient.ui.icu

import com.doctoraak.doctoraakpatient.model.IcuResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiIcu
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object IcuRepository {

    fun getIcus(latt: Double,
                lang: Double,
                isIsolationCenter: Boolean,
                pageNum: Int,
                success: (IcuResponse) -> Unit,
                loading: () -> Unit,
                errorMsg: (String) -> Unit,
                error: (Int) -> Unit) =
        ApiIcu.getIcus(
            latt, lang,
            isIsolationCenter,pageNum, object : BaseResponseListener<IcuResponse> {
                override fun onSuccess(model: IcuResponse) {
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


    fun getIcus(
        area: Int,
        city: Int,
        isIsolationCenter: Boolean,
        pageNum: Int,
        success: (IcuResponse) -> Unit,
        loading: () -> Unit,
        errorMsg: (String) -> Unit,
        error: (Int) -> Unit) =
        ApiIcu.getIcus(
            area, city, isIsolationCenter, pageNum, object : BaseResponseListener<IcuResponse> {
                override fun onSuccess(model: IcuResponse) {
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