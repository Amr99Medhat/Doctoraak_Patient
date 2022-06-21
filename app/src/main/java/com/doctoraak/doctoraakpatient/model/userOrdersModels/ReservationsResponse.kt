package com.doctoraak.doctoraakpatient.model.userOrdersModels

import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.google.gson.annotations.SerializedName

data class ReservationsResponse(
    val data: ArrayList<Reservation>
) : BaseResponse()

data class Reservation(
    val active: Int,
    val clinic: ReservationClinic,
    val clinic_id: Int,
    val created_at: String,
    val date: String,
    val id: Int,
    val notes: String,
    val part_id: Int,
    val patient: ReservationPatient,
    val patient_id: Int,
    val reservation_number: Int,
    val reservation_time: String,
    val type: String,
    val updated_at: String,
    val reservation_number_1: Int,
    val reservation_number_2: Int,
)

data class ReservationPatient(
    val active: Int,
    val address: String,
    val address_ar: String,
    val address_fr: String,
    val api_token: String,
    val birthdate: String,
    val created_at: String,
    val email: String,
    val firebase_token: String,
    val gender: String,
    val id: Int,
    val insurance_code: String,
    val insurance_id: String,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val password: String,
    val phone: String,
    val photo: String,
    val sms_code: String,
    val updated_at: String
)
data class ReservationClinic(
    val active: Int,
    @SerializedName("area_id")
    val area: String,
    val availability: Int,
    val available_days: Int,
    @SerializedName("city_id")
    val city: String,
    val created_at: String,
    val doctor: ReservationDoctor,
    val doctor_id: Int,
    val fees: String,
    val fees2: String,
    val id: Int,
    @SerializedName("lng")
    val lang: String,
    @SerializedName("lat")
    val latt: String,
    val phone: String,
    val photo: String,
    val updated_at: String,
    val waiting_time: Int
)

data class ReservationDoctor(
    val active: Int,
    val api_token: String,
    val created_at: String,
    val cv: String,
    val degree_id: Int,
    val degree_rate: String,
    val email: String,
    val firebase_token: String,
    val gender: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val password: String,
    val phone: String,
    val photo: String,
    val reservation_rate: String,
    val sms_code: String,
    val specialization_id: Int,
    val updated_at: String
)