package com.doctoraak.doctoraakpatient.repository.remote


import com.doctoraak.doctoraakpatient.model.IcuResponse
import com.doctoraak.doctoraakpatient.utils.start
import retrofit2.Call

object ApiIcu {

    fun getIcus(latt : Double, lang : Double , isIsolationCenter:Boolean, pageNum: Int,callback: BaseResponseListener<IcuResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<IcuResponse> = if (!isIsolationCenter) service.getIcus(latt,lang, pageNum)
        else service.getIsolationCenter(latt,lang, pageNum)
        call.start(callback)
    }

    fun getIcus(area : Int,
                 city : Int , isIsolationCenter:Boolean,
                pageNum: Int,callback: BaseResponseListener<IcuResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<IcuResponse> = if (!isIsolationCenter) service.getIcus(area, city, pageNum)
        else service.getIsolationCenter(area, city, pageNum)
        call.start(callback)
    }


}