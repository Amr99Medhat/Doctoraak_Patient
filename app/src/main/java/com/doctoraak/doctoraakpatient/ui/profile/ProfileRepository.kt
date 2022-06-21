package com.doctoraak.doctoraakpatient.ui.profile

import com.doctoraak.doctoraakpatient.model.UpdatedProfileRequest
import com.doctoraak.doctoraakpatient.model.UserResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object ProfileRepository {

    fun updateProfile(request : UpdatedProfileRequest , success: (UserResponse)->Unit
                   , loading: ()->Unit
                   , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.updateProfile( request,object : BaseResponseListener<UserResponse>
            {
                override fun onSuccess(model: UserResponse) {
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