package com.doctoraak.doctoraakpatient.ui.main

import com.doctoraak.doctoraakpatient.model.ClinicsResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiDoctor
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object MainRepository {

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

}