package com.doctoraak.doctoraakpatient.repository.remote


import android.util.Log
import com.doctoraak.doctoraakpatient.model.MedicineOrderRequest
import com.doctoraak.doctoraakpatient.model.PharmacyFilterRequest
import com.doctoraak.doctoraakpatient.model.PharmacyFilterResponse
import com.doctoraak.doctoraakpatient.model.PharmacyOrderResponse
import com.doctoraak.doctoraakpatient.utils.start
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

object ApiPharmacy {

    fun createPharmacyOrder(
        request: MedicineOrderRequest,
        image: String,
        callback: BaseResponseListener<PharmacyOrderResponse>) {

        val file = File(image)

        Log.d("saif", "createPharmacyOrder: request=$request")

        var part: MultipartBody.Part? = null
        if (file.exists()) {
            val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
            part = MultipartBody.Part.createFormData("photo", file.name, requestBody)
        }

        val api_token = RequestBody.create(
            MediaType.parse("text/plain"),
            request.api_token
        )

        val date = RequestBody.create(
            MediaType.parse("text/plain"),
            /*"2020-02-11"*/request.date
        )

        val pharmacyId = RequestBody.create(
            MediaType.parse("text/plain"),
            request.pharmacy_id.toString()
        )

        val patient_id = RequestBody.create(
            MediaType.parse("text/plain"),
            request.patient_id.toString()
        )


        val notes = RequestBody.create(
            MediaType.parse("text/plain"),
            request.notes
        )


        val list = RequestBody.create(
            MediaType.parse("text/plain"),
            Gson().toJson(request.orderDetails)
        )

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call: Call<PharmacyOrderResponse> = if (file.exists()) {

            service.createPharmacyOrder(
                part!!, api_token, date, pharmacyId, patient_id, notes, list
            )

        } else {
            service.createPharmacyOrder(
                api_token, date, pharmacyId, patient_id, notes, list
            )
        }
        call.start(callback)
    }

    fun filterPharmacies(request: PharmacyFilterRequest,
                         pageNum: Int,
                         callback: BaseResponseListener<PharmacyFilterResponse>) {

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val map = HashMap<String, String>()
        if (request.city != -1)
            map["city_id"] = request.city.toString()
        if (request.area != -1)
            map["area_id"] = request.area.toString()
        if (request.api_token != "")
            map["api_token"] = request.api_token
        if (request.patient_id != -1)
            map["patient_id"] = request.patient_id.toString()
        if (request.latt != -1.0)
            map["lat"] = request.latt.toString()
        if (request.lang != -1.0)
            map["lng"] = request.lang.toString()
        if (request.name.isNotEmpty())
            map["pharmacy_name"] = request.name

        map["page"] = pageNum.toString()
        map["insurance"] = request.insurance.toString()
        map["delivery"] = request.delivery.toString()

        Log.d("saif", "filterPharmacies: $map")
        val call: Call<PharmacyFilterResponse> = service.filterPharmacies(map)
        call.start(callback)
    }

    fun getAllPharmacies(callback: BaseResponseListener<PharmacyFilterResponse>) {

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<PharmacyFilterResponse> = service.getAllPharmacies()
        call.start(callback)
    }


}