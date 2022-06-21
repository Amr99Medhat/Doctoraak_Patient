package com.doctoraak.doctoraakpatient.repository.remote

import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.model.userOrdersModels.LabOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.PharmacyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.RadiologyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.ReservationsResponse
import com.doctoraak.doctoraakpatient.utils.start
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File


object ApiOthers {

    fun updateProfile(request: UpdatedProfileRequest, callback:
    BaseResponseListener<UserResponse>) {

        val file = File(request.photo)

        var part: MultipartBody.Part? = null
        if (file.exists()) {
            val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
            part = MultipartBody.Part.createFormData("photo", file.name, requestBody)
        }

        val user_id = RequestBody.create(
            MediaType.parse("text/plain"),
            request.user_id
        )

        val name = RequestBody.create(
            MediaType.parse("text/plain"),
            request.name
        )

        val gender = RequestBody.create(
            MediaType.parse("text/plain"),
            request.gender
        )

        val birthdate = RequestBody.create(
            MediaType.parse("text/plain"),
            request.birthdate
        )

        val address = RequestBody.create(
            MediaType.parse("text/plain"),
            request.address
        )

        val api_token = RequestBody.create(
            MediaType.parse("text/plain"),
            request.api_token
        )

        val patient_name = RequestBody.create(
            MediaType.parse("text/plain"),
            request.patient_name
        )

        val phone2 = RequestBody.create(
            MediaType.parse("text/plain"),
            request.phone2
        )

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call: Call<UserResponse> = if (file.exists()) {
            service.updateProfile(
                part!!, user_id, name,
                gender, birthdate, address, api_token, patient_name, phone2
            )
        } else {
            service.updateProfile(user_id, name, gender, birthdate, address, api_token, patient_name, phone2)
        }
        call.start(callback)
    }


    fun getCities(callback: BaseResponseListener<CityResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<CityResponse> = service.getCities()
        call.start(callback)
    }


    fun getAreas(callback: BaseResponseListener<AreaResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<AreaResponse> = service.getAreas()
        call.start(callback)
    }

    fun getInsurrance(callback: BaseResponseListener<InsuranceResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<InsuranceResponse> = service.getInsurances()
        call.start(callback)
    }

    fun getMedicines(callback: BaseResponseListener<MedicinesResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<MedicinesResponse> = service.getMedicines()
        call.start(callback)
    }


    fun getMedicinesType(callback: BaseResponseListener<MedicinesTypeResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<MedicinesTypeResponse> = service.getMedicinesType()
        call.start(callback)

    }

    fun getRays(callback: BaseResponseListener<RaysResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<RaysResponse> = service.getRays()
        call.start(callback)
    }

    fun getAnaylsis(callback: BaseResponseListener<AnalysisResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<AnalysisResponse> = service.getAnaylsis()
        call.start(callback)
    }

    fun getReservations(patientId: Int,
                        api_token: String,
                        callback: BaseResponseListener<ReservationsResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<ReservationsResponse> = service.getReservations(patientId, api_token)
        call.start(callback)
    }

    fun getNotifications(userId: Int,
                         userType: String,
                         api_token: String,
                         callback: BaseResponseListener<NotificationsResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<NotificationsResponse> =
            service.getNotifications(userId, userType, api_token)
        call.start(callback)
    }

    fun removeNotifications(notification_id: Int, callback: BaseResponseListener<BaseResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        service.removeNotifications(notification_id).start(callback)
    }

    fun updateFirebaseToken(firebase_token: String,
                            user_id: Int,
                            user_type: String,
                            api_token: String,
                            callback: BaseResponseListener<FirebaseTokenResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call = service.updateToken(firebase_token, user_id, user_type, api_token)
        call.start(callback)
    }

    fun getContactInfo(callback: BaseResponseListener<ContactUsResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call = service.getContactInfo()
        call.start(callback)
    }

    fun cancelOrder(api_token: String,
                    patient_id: Int,
                    order_id: Int,
                    order_type: String,
                    message: String,
                    callback: BaseResponseListener<BaseResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call = service.cancelOrder(api_token, patient_id, order_id, order_type, message)
        call.start(callback)
    }

    fun getMyOrdersDoctor(api_token: String,
                          patient_id: Int,
                          callback: BaseResponseListener<ReservationsResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call = service.getMyOrdersDoctor(patient_id, api_token, "DOCTOR")
        call.start(callback)
    }

    fun getMyOrdersPharmacy(api_token: String,
                            patient_id: Int,
                            callback: BaseResponseListener<PharmacyOrdersListResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call = service.getMyOrdersPharmacy(patient_id, api_token, "PHARMACY")
        call.start(callback)
    }

    fun getMyOrdersLab(api_token: String,
                       patient_id: Int,
                       callback: BaseResponseListener<LabOrdersListResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call = service.getMyOrdersLab(patient_id, api_token, "LAB")
        call.start(callback)
    }

    fun getMyOrdersRadiology(api_token: String,
                             patient_id: Int,
                             callback: BaseResponseListener<RadiologyOrdersListResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call = service.getMyOrdersRadiology(patient_id, api_token, "RADIOLOGY")
        call.start(callback)
    }


}