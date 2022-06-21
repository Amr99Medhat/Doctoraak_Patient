package com.doctoraak.doctoraakpatient.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("address")
    var address: String = "",
    @SerializedName("address_ar")
    var addressAr: String = "",
    @SerializedName("address_fr")
    var addressFr: String = "",
    @SerializedName("api_token")
    var apiToken: String = "",
    @SerializedName("birthdate")
    var birthdate: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("insurance_code")
    var insuranceCode: String = "",
    @SerializedName("insurance_id")
    var insuranceId: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("name_ar")
    var nameAr: String = "",
    @SerializedName("name_fr")
    var nameFr: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("sms_code")
    var smsCode: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("insurance")
    var insurance: Insurance? = null,


    // New Fields
    @SerializedName("patient_name")
    var patient_name: String = "",
    @SerializedName("phone2")
    var phone2: String = ""
)

data class UserResponse(
    @SerializedName("data")
    val user: User? = null
) : BaseResponse()

data class UpdatedProfileRequest(
    val user_id: String, val name: String, val gender: String, val birthdate: String,
    val photo: String, val address: String, val api_token: String, val patient_name: String, val phone2: String
)