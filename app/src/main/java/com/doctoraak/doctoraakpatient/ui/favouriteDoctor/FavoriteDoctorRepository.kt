package com.doctoraak.doctoraakpatient.ui.favouriteDoctor

import com.doctoraak.doctoraakpatient.model.ClinicsResponse
import com.doctoraak.doctoraakpatient.model.FavDoctorResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiDoctor
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object FavoriteDoctorRepository {

    fun getFavouriteDoctors(patientId: Int , apiToken: String,
                            success: (ClinicsResponse)->Unit
                       , loading: ()->Unit
                       , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiDoctor.getFavouriteDoctors(patientId , apiToken,
            object : BaseResponseListener<ClinicsResponse>
        {
            override fun onSuccess(model: ClinicsResponse) {
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

    fun favDoctor(patientId: Int,doctorId: Int , apiToken: String,
        success: (FavDoctorResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiDoctor.favDoctor(patientId , doctorId , apiToken ,
            object : BaseResponseListener<FavDoctorResponse> {
            override fun onSuccess(model: FavDoctorResponse) {
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