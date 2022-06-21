package com.doctoraak.doctoraakpatient.model.userOrdersModels

import com.doctoraak.doctoraakpatient.model.BaseResponse

data class RadiologyOrdersListResponse(
    val data: ArrayList<RadiologyOrder>
) : BaseResponse()

data class RadiologyOrder(
    val accept: Int,
    val created_at: String,
    val details: ArrayList<RadiologyDetail>,
    val id: Int,
    val insurance_accept: String,
    val insurance_code: Any,
    val notes: Any,
    val patient: RadiologyPatient,
    val patient_id: Int,
    val photo: String,
    val radiology_id: Int,
    val updated_at: String
)

data class RadiologyDetail(
    val created_at: String,
    val id: Int,
    val radiology_order: Int,
    val rays: Rays,
    val rays_id: Int,
    val updated_at: String
)

data class RadiologyPatient(
    val active: Int,
    val address: String,
    val address_ar: Any,
    val address_fr: Any,
    val api_token: String,
    val birthdate: String,
    val block_date: Any,
    val block_days: Any,
    val created_at: String,
    val email: String,
    val firebase_token: String,
    val gender: String,
    val id: Int,
    val insurance_code: Any,
    val insurance_code_id: Any,
    val insurance_id: Any,
    val name: String,
    val name_ar: Any,
    val name_fr: Any,
    val password: String,
    val phone: String,
    val photo: String,
    val sms_code: String,
    val updated_at: String
)

data class Rays(
    val created_at: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)