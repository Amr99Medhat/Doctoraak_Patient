package com.doctoraak.doctoraakpatient.ui.bookDoctorDetail

import com.doctoraak.doctoraakpatient.model.ClinicOrderResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiDoctor
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object BookDoctorDetailsRepository {

    fun createOrderClinic(patientId: Int, clinic_id: Int,
        date: String, type: Int, notes: String
        , api_token: String,part : Int, success: (ClinicOrderResponse) -> Unit, loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit) =
        ApiDoctor.craeteClinicOrder(patientId, clinic_id,
            date, type, notes, api_token , part
            , object : BaseResponseListener<ClinicOrderResponse> {
                override fun onSuccess(model: ClinicOrderResponse) {
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

