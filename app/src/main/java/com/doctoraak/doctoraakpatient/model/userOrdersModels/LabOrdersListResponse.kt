package com.doctoraak.doctoraakpatient.model.userOrdersModels

import com.doctoraak.doctoraakpatient.model.BaseResponse

data class LabOrdersListResponse(
    val data: ArrayList<LabOrder>
) : BaseResponse()

data class LabOrder(
    val accept: Int,
    val created_at: String,
    val details: ArrayList<LabDetail>,
    val id: Int,
    val insurance_accept: String,
    val insurance_code: Any,
    val lab_id: Int,
    val notes: Any,
    val patient: LabPatient,
    val patient_id: Int,
    val photo: String,
    val updated_at: String
)

data class Analysis(
    val created_at: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)

data class LabDetail(
    val analysis: Analysis,
    val analysis_id: Int,
    val created_at: String,
    val id: Int,
    val lab_order: Int,
    val updated_at: String
)

data class LabPatient(
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