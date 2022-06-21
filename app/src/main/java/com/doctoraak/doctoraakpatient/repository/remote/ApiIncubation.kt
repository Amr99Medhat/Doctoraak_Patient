package com.doctoraak.doctoraakpatient.repository.remote


import com.doctoraak.doctoraakpatient.model.IncubationResponse
import com.doctoraak.doctoraakpatient.utils.start
import retrofit2.Call

object ApiIncubation {

    fun getIncubations( latt : Double,
                      lang : Double, pageNum:Int ,callback: BaseResponseListener<IncubationResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<IncubationResponse> = service.getIncubation(latt,lang, pageNum)
        call.start(callback)

    }

    fun getIncubations( area : Int,
                        city : Int, pageNum:Int ,callback: BaseResponseListener<IncubationResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<IncubationResponse> = service.getIncubation(area,city, pageNum)
        call.start(callback)
    }


}