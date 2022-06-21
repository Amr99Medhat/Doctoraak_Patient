package com.doctoraak.doctoraakpatient.ui.searchDoctor

import com.doctoraak.doctoraakpatient.model.ClinicFilter
import com.doctoraak.doctoraakpatient.model.ClinicsResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiDoctor
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener
import com.doctoraak.doctoraakpatient.utils.DoctorType

object SearchDoctorRepository
{

    fun getClinics(clinicFilter: ClinicFilter, doctorType: DoctorType, pageNum: Int, success: (ClinicsResponse)->Unit
                   , loading: ()->Unit
                   , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiDoctor.getClinics(clinicFilter, doctorType, pageNum
            ,object : BaseResponseListener<ClinicsResponse>
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