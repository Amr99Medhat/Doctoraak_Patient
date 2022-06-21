package com.doctoraak.doctoraakpatient.model

import com.google.gson.annotations.SerializedName

data class ClinicsResponse(
    val data: ArrayList<Clinic>
) : BaseResponse()

data class ClinicOrderResponse(
    val data: String) :BaseResponse()

data class Clinic (
    val active: Int,
    @SerializedName("area_id")
    val area: String,
    val availability: Int,
    val available_days: Int,
    val address: String,
    @SerializedName("city_id")
    val city: String,
    val created_at: String,
    val degree: Degree?,
    val notes: String?,
    val distance: Double,
    val doctor: Doctor,
    val doctor_id: Int,
    val fees: String,
    val fees2: String,
    val id: Int,
    val lang: String,
    val latt: String,
    val phone: String,
    val photo: String,
    val specialization: Specialization?,
    val updated_at: String,
    val waiting_time: Int,
    val working_hours: ArrayList<WorkingHour>,
    val free_days: ArrayList<FreeDays>
)

data class FreeDays(val date: String,
                    val part_id: Int)

data class Degree(
    val created_at: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)


data class Specialization(
    val created_at: String,
    val icon: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)


data class Doctor(
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
    val updated_at: String,
    val rate: String,
    var title: String = ""
)


data class WorkingHour(
    val active: Int,
    val clinic_id: Int,
    val created_at: String,
    val day: Int,
    val id: Int,
    val part1_from: String,
    val part1_to: String,
    val part2_from: String,
    val part2_to: String,
    val reservation_number_1: Int,
    val reservation_number_2: Int,
    val updated_at: String
)

data class ClinicFilter(
    val city_id : Int= -1,
    val area_id : Int = -1,
    val insurance : Int = 0,
    val delivery : Int = 0,
    val patient_id : Int = -1,
    val specialization_id : Int = -1,
    val api_token : String = "",
    val latt :Double = -1.0,
    val lang :Double = -1.0,
    val isMedicalCenter : Int = 0
)