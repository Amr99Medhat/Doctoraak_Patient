package com.doctoraak.doctoraakpatient.model.userOrdersModels

import com.doctoraak.doctoraakpatient.model.BaseResponse

data class PharmacyOrdersListResponse(
    val data: ArrayList<PharmacyOrders>
) :BaseResponse()

data class PharmacyOrders(
    val created_at: String,
    val details: ArrayList<Detail>,
    val id: Int,
    val insurance_accept: String,
    val insurance_code: Any,
    val notes: Any,
    val patient: Patient,
    val patient_id: Int,
    val pharmacy_id: Any,
    val photo: String,
    val updated_at: String
)

data class Detail(
    val amount: Int,
    val created_at: String,
    val id: Int,
    val medicine: Medicine,
    val medicine_id: Int,
    val medicine_type: MedicineType,
    val medicine_type_id: Int,
    val pharmacy_order: Int,
    val updated_at: String
)

data class Medicine(
    val created_at: Any,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: Any
)

data class MedicineType(
    val created_at: Any,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: Any
)

data class Patient(
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