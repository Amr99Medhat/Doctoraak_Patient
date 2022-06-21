package com.doctoraak.doctoraakpatient.repository.remote


import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.utils.start
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

object ApiRadiology {


    fun createRadiologyOrder(request : RadiologyOrderRequest
                       , callback: BaseResponseListener<RadiologyOrderResponse>) {

        val file = File(request.photo)

        var part : MultipartBody.Part? = null
        if (file.exists()) {
            val requestBody = RequestBody.create(MediaType.parse("image/*"),file)
            part = MultipartBody.Part.createFormData("photo",file.name , requestBody)
        }

        val api_token = RequestBody.create(
            MediaType.parse("text/plain"),
            request.api_token
        )

        val radiology_id = RequestBody.create(
            MediaType.parse("text/plain"),
            request.radiology_id.toString()
        )

        val date = RequestBody.create(
            MediaType.parse("text/plain"),
            request.date.toString()
        )

        val patient_id = RequestBody.create(
            MediaType.parse("text/plain"),
            request.patient_id.toString()
        )

        val notes = RequestBody.create(
            MediaType.parse("text/plain"),
            request.notes.toString()
        )


        val list = RequestBody.create(
            MediaType.parse("text/plain"),
            Gson().toJson(request.orderDetails)
        )

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<RadiologyOrderResponse> = if (request.orderDetails.size > 0){

            service.createRadiologyOrder( api_token
                ,radiology_id , date,patient_id ,notes , list)

        }else{

            service.createRadiologyOrder(part!!,api_token
                ,radiology_id , date,patient_id ,notes ,list)
        }
        call.start(callback)
    }

    fun filterRadiologies(filter : RadiologyFilter, pageNum: Int
                          , callback: BaseResponseListener<RadiologyResponse>
    ) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val map = HashMap<String , String >()
        if (filter.city_id != -1)
            map["city_id"] = filter.city_id.toString()
        if (filter.area_id != -1)
            map["area_id"] = filter.area_id.toString()
        if (filter.patient_id != -1)
            map["patient_id"] = filter.patient_id.toString()
        if (filter.name.isNotEmpty())
            map["radiology_name"] = filter.name
        if (filter.api_token != "")
            map["api_token"] = filter.api_token
        if (filter.lang != -1.0)
            map["lng"] = filter.lang.toString()
        if (filter.latt != -1.0)
            map["lat"] = filter.latt.toString()

        map["page"] = pageNum.toString()
        map["delivery"] = filter.delivery.toString()
        map["insurance"] = filter.insurance.toString()
        val call: Call<RadiologyResponse> = service.filterRadiologies(map)
        call.start(callback)
    }

    fun getAllRadiologies(callback: BaseResponseListener<AllRadiologiesResponse>
    ) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<AllRadiologiesResponse> = service.getAllRadiology()
        call.start(callback)
    }




}