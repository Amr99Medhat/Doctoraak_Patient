package com.doctoraak.doctoraakpatient.model

data class ResendSMSCodeResponse(
    val data: SMScodeInfo
) : BaseResponse()

data class SMScodeInfo(
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