package com.doctoraak.doctoraakpatient.repository.remote


import android.util.Log
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.utils.DoctorType
import com.doctoraak.doctoraakpatient.utils.start
import com.doctoraak.doctoraakpatient.utils.toInt
import retrofit2.Call

object ApiDoctor {


    fun getFavouriteDoctors(
        patientId: Int, apiToken: String,
        callback: BaseResponseListener<ClinicsResponse>
    ) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<ClinicsResponse> = service.getFavouriteDocotors(patientId, apiToken)
        call.start(callback)
    }

    fun favDoctor(
        patientId: Int, doctorId: Int, apiToken: String
        , callback: BaseResponseListener<FavDoctorResponse>
    ) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<FavDoctorResponse> = service.favDoctor(patientId, doctorId, apiToken)
        call.start(callback)
    }

    fun rateDoctor(
        patientId: Int, doctorId: Int, rate: Int, apiToken: String, type: String
        , callback: BaseResponseListener<RatingResponse>
    ) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<RatingResponse> =
            service.rateDoctor(patientId, doctorId, if (rate < 3) 3 else rate, apiToken, type)
        call.start(callback)
    }

    fun getCategories(callback: BaseResponseListener<DoctorCategoryResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<DoctorCategoryResponse> = service.getDoctorCategories()
        call.start(callback)
    }

    ////////////////////////////////////
    fun getClinics(clinicFilter: ClinicFilter, doctorType: DoctorType, pageNum: Int, callback: BaseResponseListener<ClinicsResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val map = HashMap<String, String>()
        if (clinicFilter.city_id != -1)
            map["city_id"] = clinicFilter.city_id.toString()
        if (clinicFilter.area_id != -1)
            map["area_id"] = clinicFilter.area_id.toString()
        if (clinicFilter.patient_id != -1)
            map["patient_id"] = clinicFilter.patient_id.toString()
        if (clinicFilter.specialization_id != -1)
            map["specialization_id"] = clinicFilter.specialization_id.toString()
        if (clinicFilter.api_token != "")
            map["api_token"] = clinicFilter.api_token
        if (clinicFilter.lang != -1.0)
            map["lng"] = clinicFilter.lang.toString()
        if (clinicFilter.latt != -1.0)
            map["lat"] = clinicFilter.latt.toString()

        map["page"] = pageNum.toString()
        map["insurance"] = clinicFilter.insurance.toString()
        map["isHospital"] = 0.toString()
        map["is_medical_center"] = 0.toString()
        map["is_optical_center"] = 0.toString()

        when(doctorType)
        {
            DoctorType.HOSPITAL -> map["isHospital"] = 1.toString()
            DoctorType.MEDICAL_CENTER -> map["is_medical_center"] = 1.toString()
            DoctorType.OPTICAL_CENTER -> map["is_optical_center"] = 1.toString()
        }

        Log.d("saif", "getClinics: filter= $map")
        val call: Call<ClinicsResponse> = service.getClinics(map)
        call.start(callback)
    }
    ///////////////////////////////////

    fun craeteClinicOrder(
        patientId: Int,
        clinic_id: Int,
        date: String,
        type: Int
        , notes: String
        , api_token: String, part: Int, callback: BaseResponseListener<ClinicOrderResponse>
    ) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<ClinicOrderResponse> = service.craeteClinicOrder(
            patientId, clinic_id
            , date, type, notes, api_token, part
        )
        call.start(callback)
    }


}