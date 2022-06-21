package com.doctoraak.doctoraakpatient.repository.remote

import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.model.userOrdersModels.LabOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.PharmacyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.RadiologyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.ReservationsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiEndPoint {

    @GET("show_insurance")
    fun getInsurances(): Call<InsuranceResponse>

    @FormUrlEncoded
    @POST("patient_resend")
    fun resendSmsCode(@Field("user_id") user_id: Int
    ): Call<ResendSMSCodeResponse>

    @FormUrlEncoded
    @POST("patient_login")
    fun logIn(@Field("phone") phone: String,
              @Field("password") password: String): Call<UserResponse>

    @Multipart
    @POST("patient_register")
    fun register(
        @Part photo: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("address") address: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("insurance_id") insuranceId: RequestBody,
        @Part("insurance_code_id") insurance_code_id: RequestBody): Call<UserResponse>

    @Multipart
    @POST("patient_register")
    fun register(@Part photo: MultipartBody.Part?,
                 @Part("name") name: RequestBody,
                 @Part("password") password: RequestBody,
                 @Part("address") address: RequestBody,
                 @Part("gender") gender: RequestBody,
                 @Part("email") email: RequestBody,
                 @Part("phone") phone: RequestBody,
                 @Part("birthdate") birthdate: RequestBody
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("patient_forget_password")
    fun forgetPassword(@Field("phone") phone: String): Call<BaseResponse>

    @FormUrlEncoded
    @POST("patient_update_password")
    fun forgetPasswordUpdatePassword(
        @Field("phone") phone: String,
        @Field("old_password") password: String,
        @Field("new_password") new_password: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("patient_verify_account")
    fun activeAccount(@Field("user_id") user_id: Long,
                      @Field("sms_code") sms_code: String): Call<UserResponse>

    @GET("show_specialization")
    fun getDoctorCategories(): Call<DoctorCategoryResponse>

    //all areas
    @GET("show_area")
    fun getAreas(): Call<AreaResponse>

    //areas for specific city
    @GET("show_area")
    fun getAreas(@Field("city_id") city_id: Int): Call<AreaResponse>

    @GET("show_city")
    fun getCities(): Call<CityResponse>

    @GET("show_rays")
    fun getRays()
            : Call<RaysResponse>

    @GET("show_anlysis")
    fun getAnaylsis()
            : Call<AnalysisResponse>

    @GET("show_medicines")
    fun getMedicines()
            : Call<MedicinesResponse>

    @GET("show_medicines_type")
    fun getMedicinesType()
            : Call<MedicinesTypeResponse>

    @GET("patient/favourite/list")
    fun getFavouriteDocotors(
        @Query("patient_id") patientId: Int, @Query("api_token") api_token: String
    ): Call<ClinicsResponse>


    @GET("clinic/get")
    fun getClinics(@QueryMap map: HashMap<String, String>): Call<ClinicsResponse>

    ////////////////////////////////////


    @GET("incubation/get")
    fun getIncubation(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("page") pageNum: Int
    ): Call<IncubationResponse>

    @GET("incubation/get")
    fun getIncubation(
        @Query("area_id") area: Int,
        @Query("city_id") city: Int,
        @Query("page") pageNum: Int
    ): Call<IncubationResponse>

    @GET("isolation-center/get")
    fun getIsolationCenter(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("page") pageNum: Int
    ): Call<IcuResponse>

    @GET("icu/get")
    fun getIcus(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("page") pageNum: Int
    ): Call<IcuResponse>

    @GET("get/resrevation")
    fun getReservations(
        @Query("patient_id") patient_id: Int, @Query("api_token") api_token: String
    ): Call<ReservationsResponse>

    @GET("icu/get")
    fun getIcus(
        @Query("area_id") area: Int,
        @Query("city_id") city: Int,
        @Query("page") pageNum: Int
    ): Call<IcuResponse>

    @GET("isolation-center/get")
    fun getIsolationCenter(
        @Query("area_id") area: Int,
        @Query("city_id") city: Int,
        @Query("page") pageNum: Int
    ): Call<IcuResponse>

    @POST("clinic/order/create")
    fun craeteClinicOrder(
        @Query("patient_id") patientId: Int,
        @Query("clinic_id") clinic_id: Int,
        @Query("date") date: String,
        @Query("type") type: Int,
        @Query("notes") notes: String,
        @Query("api_token") api_token: String,
        @Query("part") part: Int): Call<ClinicOrderResponse>


    @GET("lab/get")
    fun filterLabs(@QueryMap map: HashMap<String, String>): Call<LabResponse>

    @GET("radiology/get")
    fun filterRadiologies(@QueryMap map: HashMap<String, String>): Call<RadiologyResponse>

    @Multipart
    @POST("pharmacy/order/create")
    fun createPharmacyOrder(
        @Part image: MultipartBody.Part,
        @Part("api_token") api_token: RequestBody,
        @Part("date") date: RequestBody,
        @Part("pharmacy_id") pharmacy_id: RequestBody,
        @Part("patient_id") patient_id: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("orderDetails") orderDetails: RequestBody
    ): Call<PharmacyOrderResponse>


    @Multipart
    @POST("pharmacy/order/create")
    fun createPharmacyOrder(
        @Part("api_token") api_token: RequestBody,
        @Part("date") date: RequestBody,
        @Part("pharmacy_id") pharmacy_id: RequestBody,
        @Part("patient_id") patient_id: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("orderDetails") orderDetails: RequestBody
    ): Call<PharmacyOrderResponse>


    @Multipart
    @POST("lab/order/create")
    fun createLabOrder(
        @Part image: MultipartBody.Part,
        @Part("api_token") api_token: RequestBody,
        @Part("lab_id") lab_id: RequestBody,
        @Part("date") date: RequestBody,
        @Part("patient_id") patient_id: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("orderDetails") orderDetails: RequestBody

    ): Call<LabOrderResponse>


    @Multipart
    @POST("lab/order/create")
    fun createLabOrder(@Part("api_token") api_token: RequestBody,
                       @Part("lab_id") lab_id: RequestBody,
                       @Part("date") date: RequestBody,
                       @Part("patient_id") patient_id: RequestBody,
                       @Part("notes") notes: RequestBody,
                       @Part("orderDetails") orderDetails: RequestBody
    ): Call<LabOrderResponse>

    @Multipart
    @POST("radiology/order/create")
    fun createRadiologyOrder(
        @Part image: MultipartBody.Part,
        @Part("api_token") api_token: RequestBody,
        @Part("radiology_id") radiology_id: RequestBody,
        @Part("date") date: RequestBody,
        @Part("patient_id") patient_id: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("orderDetails") orderDetails: RequestBody

    ): Call<RadiologyOrderResponse>


    @Multipart
    @POST("radiology/order/create")
    fun createRadiologyOrder(@Part("api_token") api_token: RequestBody,
                             @Part("radiology_id") radiology_id: RequestBody,
                             @Part("date") date: RequestBody,
                             @Part("patient_id") patient_id: RequestBody,
                             @Part("notes") notes: RequestBody,
                             @Part("orderDetails") orderDetails: RequestBody

    ): Call<RadiologyOrderResponse>


    @Multipart
    @POST("patient_update_profile")
    fun updateProfile(
        //@Part image: MultipartBody.Part,
        @Part("user_id") user_id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("address") address: RequestBody,
        @Part("api_token") api_token: RequestBody,
        @Part("patient_name") patient_name: RequestBody,
        @Part("phone2") phone2: RequestBody
    ): Call<UserResponse>

    @Multipart
    @POST("patient_update_profile")
    fun updateProfile(
        @Part image: MultipartBody.Part,
        @Part("user_id") user_id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("address") address: RequestBody,
        @Part("api_token") api_token: RequestBody,
        @Part("patient_name") patient_name: RequestBody,
        @Part("phone2") phone2: RequestBody
    ): Call<UserResponse>


    @POST("patient/favourite_doctor")
    fun favDoctor(
        @Query("patient_id") patient_id: Int, @Query("doctor_id") doctor_id: Int,
        @Query("api_token") api_token: String
    ): Call<FavDoctorResponse>

    //get all labs with their ids
    @GET("show/lab")
    fun getAllLabs(): Call<AllLabsResponse>

    //get all radiologies with their ids
    @GET("show/radiology")
    fun getAllRadiology(): Call<AllRadiologiesResponse>

    @GET("get/notification")
    fun getNotifications(@Query("user_id") user_id: Int,
                         @Query("user_type") user_type: String,
                         @Query("api_token") api_token: String)
            : Call<NotificationsResponse>

    @POST("notification/remove")
    fun removeNotifications(@Query("notification_id") notification_id: Int)
            : Call<BaseResponse>

    @POST("patient/rate/doctor")
    fun rateDoctor(
        @Query("patient_id") patient_id: Int,
        @Query("doctor_id") doctor_id: Int,
        @Query("rate") rate: Int,
        @Query("api_token") api_token: String,
        @Query("type") type: String
    ): Call<RatingResponse>

    @POST("token/update")
    fun updateToken(@Query("firebase_token") firebase_token: String,
                    @Query("user_id") user_id: Int,
                    @Query("user_type") user_type: String,
                    @Query("api_token") api_token: String)
            : Call<FirebaseTokenResponse>

    @GET("contact")
    fun getContactInfo(): Call<ContactUsResponse>

    @POST("patient/cancel-order")
    fun cancelOrder(@Query("api_token") api_token: String,
                    @Query("patient_id") patient_id: Int,
                    @Query("order_id") order_id: Int,
                    @Query("order_type") order_type: String,
                    @Query("message") message: String): Call<BaseResponse>


    @GET("get-orders")
    fun getMyOrdersDoctor(@Query("patient_id") patient_id: Int,
                          @Query("api_token") api_token: String,
                          @Query("user_type") user_type: String): Call<ReservationsResponse>

    @GET("get-orders")
    fun getMyOrdersPharmacy(@Query("patient_id") patient_id: Int,
                            @Query("api_token") api_token: String,
                            @Query("user_type") user_type: String): Call<PharmacyOrdersListResponse>

    @GET("get-orders")
    fun getMyOrdersLab(@Query("patient_id") patient_id: Int,
                       @Query("api_token") api_token: String,
                       @Query("user_type") user_type: String): Call<LabOrdersListResponse>

    @GET("get-orders")
    fun getMyOrdersRadiology(@Query("patient_id") patient_id: Int,
                             @Query("api_token") api_token: String,
                             @Query("user_type") user_type: String): Call<RadiologyOrdersListResponse>

    @GET("pharmacy/get")
    fun filterPharmacies(@QueryMap map: HashMap<String, String>): Call<PharmacyFilterResponse>

    @GET("show/pharmacy")
    fun getAllPharmacies(): Call<PharmacyFilterResponse>

    //Payment
    @GET("show-cariense-details")
    fun getPaymentDetails(): Call<PaymentDetailResponse>

    @GET("patient-buy-careiense")
    fun applayBuyCareience(@Query("user_id") userId: Int): Call<UserResponse>

    @POST("auth/tokens")
    fun paymentAuth(@Body body: PaymentAuthRequest): Call<PaymentAuthResponse>

    @POST("ecommerce/orders")
    fun orderRegisteration(@Body body: OrderRegisterationRequest): Call<OrderRegisterationResponse>

    @POST("acceptance/payment_keys")
    fun getPaymentToken(@Body body: CardPaymentKeyRequest): Call<CardPaymentKeyResposne>

    @POST("acceptance/payments/pay")
    fun makePayRequest(@Body body: PayOrderRequest): Call<PayOrderResponse>
}
