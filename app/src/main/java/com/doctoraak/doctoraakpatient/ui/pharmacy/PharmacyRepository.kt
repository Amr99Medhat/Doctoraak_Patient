package com.doctoraak.doctoraakpatient.ui.pharmacy

import com.doctoraak.doctoraakpatient.model.MedicineOrderRequest
import com.doctoraak.doctoraakpatient.model.PharmacyFilterRequest
import com.doctoraak.doctoraakpatient.model.PharmacyFilterResponse
import com.doctoraak.doctoraakpatient.model.PharmacyOrderResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiPharmacy
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object PharmacyRepository {

    fun createPharmacyOrder(
        request: MedicineOrderRequest
        , s: String, success: (PharmacyOrderResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiPharmacy.createPharmacyOrder(
            request, s, object : BaseResponseListener<PharmacyOrderResponse> {
                override fun onSuccess(model: PharmacyOrderResponse) {
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

    fun getFilteredPharmacies(pharmacyFilterRequest: PharmacyFilterRequest, pageNum: Int, success: (PharmacyFilterResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiPharmacy.filterPharmacies(
            pharmacyFilterRequest, pageNum,
            object : BaseResponseListener<PharmacyFilterResponse> {
                override fun onSuccess(model: PharmacyFilterResponse) {
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

    fun getAllPharmacies(success: (PharmacyFilterResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiPharmacy.getAllPharmacies(
            object : BaseResponseListener<PharmacyFilterResponse> {
                override fun onSuccess(model: PharmacyFilterResponse) {
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