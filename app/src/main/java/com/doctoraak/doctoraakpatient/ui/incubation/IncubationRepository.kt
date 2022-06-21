package com.doctoraak.doctoraakpatient.ui.incubation

import com.doctoraak.doctoraakpatient.model.IncubationResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiIncubation
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object IncubationRepository {

    fun getIncubation(latt : Double,
                       lang : Double, pageNum:Int ,success: (IncubationResponse)->Unit
                       , loading: ()->Unit
                       , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiIncubation.getIncubations(
            latt , lang, pageNum ,object : BaseResponseListener<IncubationResponse>
        {
            override fun onSuccess(model: IncubationResponse) {
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

    fun getIncubation(area : Int,
                       city : Int, pageNum:Int ,success: (IncubationResponse)->Unit
                       , loading: ()->Unit
                       , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiIncubation.getIncubations(
            area , city, pageNum ,object : BaseResponseListener<IncubationResponse>
            {
                override fun onSuccess(model: IncubationResponse) {
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