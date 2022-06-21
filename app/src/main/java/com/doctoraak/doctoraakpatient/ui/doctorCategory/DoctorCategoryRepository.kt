package com.doctoraak.doctoraakpatient.ui.doctorCategory

import com.doctoraak.doctoraakpatient.model.DoctorCategoryResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiDoctor
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object DoctorCategoryRepository {
    fun getCategories( success: (DoctorCategoryResponse)->Unit
                      , loading: ()->Unit
                 , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiDoctor.getCategories( object : BaseResponseListener<DoctorCategoryResponse>
        {
            override fun onSuccess(model: DoctorCategoryResponse) {
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