package com.doctoraak.doctoraakpatient.repository.remote


import android.util.Log
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.utils.start
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

object ApiLab {


    fun createLabOrder(request : LabOrderRequest
                       , callback: BaseResponseListener<LabOrderResponse>) {

        Log.d("mfvkfvd" , Gson().toJson(request))
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

        val lab_id = RequestBody.create(
            MediaType.parse("text/plain"),
            request.lab_id.toString()
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

        val call: Call<LabOrderResponse> = if (file.exists()){

            service.createLabOrder(part!!,api_token
                ,lab_id , date,patient_id ,notes ,list)

        }else{

            service.createLabOrder(api_token
                ,lab_id , date,patient_id ,notes,list)
        }
        call.start(callback)
    }




    fun filterLabs(filter : LabFilter, pageNum: Int, callback: BaseResponseListener<LabResponse>
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
            map["lab_name"] = filter.name
        if (filter.api_token != "")
            map["api_token"] = filter.api_token
        if (filter.lang != -1.0)
            map["lng"] = filter.lang.toString()
        if (filter.latt != -1.0)
            map["lat"] = filter.latt.toString()

        map["page"] = pageNum.toString()
        map["delivery"] = filter.delivery.toString()
        map["insurance"] = filter.insurance.toString()

        Log.d("saif", "filterLabs  filter= $map")
        val call: Call<LabResponse> = service.filterLabs(map)
        call.start(callback)
    }
    

    fun getAllLabs(callback: BaseResponseListener<AllLabsResponse>
    ) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<AllLabsResponse> = service.getAllLabs()
        call.start(callback)
    }



}