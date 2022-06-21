package com.doctoraak.doctoraakpatient.ui.findServcie

import com.doctoraak.doctoraakpatient.model.RatingResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.ReservationsResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiDoctor
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object FindServiceRepository {

    fun getReservations(patientId: Int,
        apiToken: String, success: (ReservationsResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiOthers.getReservations(patientId, apiToken,
            object : BaseResponseListener<ReservationsResponse> {
                override fun onSuccess(model: ReservationsResponse) {
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


    fun rateDoctor(
        patientId: Int, doctorId: Int, rate: Int, apiToken: String, type: String
        , success: (RatingResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiDoctor.rateDoctor(patientId, doctorId, rate, apiToken, type,
            object : BaseResponseListener<RatingResponse> {
                override fun onSuccess(model: RatingResponse) {
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