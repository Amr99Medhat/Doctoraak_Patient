package com.doctoraak.doctoraakpatient.model

import com.doctoraak.doctoraakpatient.adapters.AutoCompleteBase
import com.google.gson.annotations.SerializedName

data class LabResponse(
    val data: ArrayList<Lab>
) :BaseResponse()

data class AllLabsResponse(
    val data: ArrayList<Lab>
) : BaseResponse()

data class LabOrderResponse(val data : String)
    : BaseResponse()


data class Lab(
    var active: Int = 0 ,
    var api_token: String = "",
    @SerializedName("area_id")
    var area: Int = 0,
    var avaliable_days: Int = 0,
    @SerializedName("city_id")
    var city: Int = 0,
    var created_at: String = "",
    var delivery: Int = 0,
    var email: String = "",
    var firebase_token: String = "",
    var id: Int = 0,
    var lab_doctor_id: Int = 0,
    @SerializedName("lng")
    var lang: String = "",
    @SerializedName("lat")
    var latt: String = "",
    var password: String = "",
    var phone: String = "",
    var phone2: String = "",
    var photo: String = "",
    var sms_code: String = "",
    var updated_at: String = "",
    var working_hours: ArrayList<LabWorkingHour> = arrayListOf()
): AutoCompleteBase {
    @SerializedName("name")
    override var name: String = ""
    @SerializedName("name_ar")
    override var nameAr: String = ""
    @SerializedName("name_fr")
    override var nameFr: String = ""
}

data class LabWorkingHour(
    val active: Int,
    val created_at: String,
    val day: Int,
    val id: Int,
    val lab_id: Int,
    val part_from: String,
    val part_to: String,
    val updated_at: String
)

data class LabOrderRequest(
    @SerializedName("api_token") val api_token : String,
    @SerializedName("lab_id") val lab_id : Int,
    @SerializedName("date") val date : String,
    @SerializedName("patient_id") val patient_id : Int,
    @SerializedName("notes") val notes : String,
    @SerializedName("photo") val photo : String,
    @SerializedName("orderDetails") val orderDetails : ArrayList<LabOrderDetail>

)

data class LabOrderDetail(
    @SerializedName("analysis_id") val analysis_id : Int
)

data class LabFilter(
    val city_id : Int= -1,
    val area_id : Int = -1,
    val insurance : Int = 0,
    val delivery : Int = 0,
    val patient_id : Int = -1,
    val api_token : String = "",
    val latt :Double = -1.0,
    val lang :Double = -1.0,
    val name :String = ""
)