package com.doctoraak.doctoraakpatient.model

import com.doctoraak.doctoraakpatient.adapters.AutoCompleteBase
import com.google.gson.annotations.SerializedName

data class RadiologyOrderResponse(val data : String)
    : BaseResponse()

data class RadiologyResponse(
    val data: ArrayList<Radiology>
) : BaseResponse()
data class Radiology(
    val active: Int,
    val api_token: String,
    @SerializedName("area_id")
    val area: Int,
    val avaliable_days: Int,
    @SerializedName("city_id")
    val city: Int,
    val created_at: String,
    val degree_rate: String,
    val delivery: Int,
    val email: String,
    val firebase_token: String,
    val id: Int,
    @SerializedName("lng")
    val lang: String,
    @SerializedName("lat")
    val latt: String,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val password: String,
    val phone: String,
    val phone2: String?,
    val photo: String,
    val radiology_doctor_id: Int,
    val reservation_rate: String,
    val sms_code: String,
    val updated_at: String,
    val working_hours: ArrayList<RadiologyWorkingHour>
)

data class RadiologyWorkingHour(
    val active: Int,
    val created_at: String,
    val day: Int,
    val id: Int,
    val part_from: String,
    val part_to: String,
    val radiology_id: Int,
    val updated_at: String
)

data class RadiologyOrderRequest(

    @SerializedName("api_token") val api_token : String,
    @SerializedName("radiology_id") val radiology_id : Int,
    @SerializedName("date") val date : String,
    @SerializedName("patient_id") val patient_id : Int,
    @SerializedName("notes") val notes : String,
    @SerializedName("photo") val photo : String,
    @SerializedName("orderDetails") val orderDetails : List<RadiologyOrderDetail>
)

data class RadiologyOrderDetail(
    @SerializedName("rays_id") val rays_id : Int
)

//////////////////////////////////////////////////////////////////
data class AllRadiologiesResponse(
    val data: ArrayList<RadiologyInfo>
) : BaseResponse()

data class RadiologyInfo(
    var active: Int = 0,
    var api_token: String = "",
    @SerializedName("area_id")
    var area: Int = 0,
    var avaliable_days: Int = 0,
    @SerializedName("city_id")
    var city: Int = 0,
    var created_at: String = "",
    var degree_rate: String = "",
    var delivery: String = "",
    var email: String = "",
    var firebase_token: String = "",
    var id: Int = 0,
    @SerializedName("lng")
    var lang: String = "",
    @SerializedName("lat")
    var latt: String = "",
    var password: String = "",
    var phone: String = "",
    var phone2: String = "",
    var photo: String = "",
    var radiology_doctor_id: Int = 0,
    var reservation_rate: String = "",
    var sms_code: String = "",
    val updated_at: String = ""
) : AutoCompleteBase {
    @SerializedName("name")
    override var name: String = ""
    @SerializedName("name_ar")
    override var nameAr: String = ""
    @SerializedName("name_fr")
    override var nameFr: String = ""
}

data class RadiologyFilter(
    val city_id : Int = -1,
    val area_id : Int = -1,
    val insurance : Int = 0,
    val delivery : Int = 0,
    val patient_id : Int = -1,
    val api_token : String = "",
    val name :String = "",
    val latt :Double = -1.0,
    val lang :Double = -1.0
)