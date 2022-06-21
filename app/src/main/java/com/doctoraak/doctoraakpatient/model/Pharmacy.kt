package com.doctoraak.doctoraakpatient.model

import com.doctoraak.doctoraakpatient.adapters.AutoCompleteBase
import com.google.gson.annotations.SerializedName

class PharmacyOrderResponse(
    val data: String
) :BaseResponse()


data class PharmacyFilterResponse(
    @SerializedName("data")
    var data: ArrayList<Pharmacy> = ArrayList<Pharmacy>()
) : BaseResponse()

data class Pharmacy(
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
    @SerializedName("area_id")
    var area: Int = 0,
    @SerializedName("avaliable_days")
    var avaliableDays: Int = 0,
    @SerializedName("city_id")
    var city: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("delivery")
    var delivery: Int = 0,
    @SerializedName("email")
    var email: String = "",
    @SerializedName("firebase_token")
    var firebaseToken: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("insurance_company")
    var insuranceCompany: List<InsuranceCompany> = listOf(),
    @SerializedName("lng")
    var lang: String = "",
    @SerializedName("lat")
    var latt: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("pharmacy_doctor_id")
    var pharmacyDoctorId: Any = Any(),
    @SerializedName("pharmacy_insurances")
    var pharmacyInsurances: List<PharmacyInsurance> = listOf(),
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("phone2")
    var phone2: Any = Any(),
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("sms_code")
    var smsCode: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("working_hours")
    var workingHours: List<PharmacyWorkingHour> = listOf()
) : AutoCompleteBase {
    @SerializedName("name")
    override var name: String = ""
    @SerializedName("name_ar")
    override var nameAr: String = ""
    @SerializedName("name_fr")
    override var nameFr: String = ""
}



data class InsuranceCompany(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("name_ar")
    var nameAr: String = "",
    @SerializedName("name_fr")
    var nameFr: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("url")
    var url: String = ""
)

data class PharmacyInsurance(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("insurance_id")
    var insuranceId: Int = 0,
    @SerializedName("pharmacy_id")
    var pharmacyId: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class PharmacyWorkingHour(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("day")
    var day: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("part_from")
    var partFrom: String = "",
    @SerializedName("part_to")
    var partTo: String = "",
    @SerializedName("pharmacy_id")
    var pharmacyId: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class PharmacyFilterRequest(
    @SerializedName("city_id") val city: Int = -1,
    @SerializedName("area_id") val area: Int = -1,
    val api_token: String = ""
    , val patient_id: Int = -1
    , val insurance: Int = -1,
    val delivery: Int = -1
    , @SerializedName("lat") val latt: Double = -1.0,
    @SerializedName("lng") val lang: Double = -1.0,
    @SerializedName("pharmacy_name") val name: String = "")
data class MedicineOrderRequest(

    @SerializedName("api_token") val api_token : String,
    @SerializedName("patient_id") val patient_id : Int,
    @SerializedName("notes") val notes : String,
    @SerializedName("photo") val photo : String,
    @SerializedName("orderDetails") val orderDetails : ArrayList<OrderDetail>,
    @SerializedName("date") val date : String = "",
    @SerializedName("pharmacy_id") val pharmacy_id : Int
)

data class OrderDetail(
    @SerializedName("medicine_id") val medicine_id : Int,
    @SerializedName("medicine_type_id") val medicine_type_id : Int,
    @SerializedName("amount") val amount : Int
)