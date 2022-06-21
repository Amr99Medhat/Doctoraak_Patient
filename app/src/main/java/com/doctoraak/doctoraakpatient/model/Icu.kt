package com.doctoraak.doctoraakpatient.model

import com.google.gson.annotations.SerializedName

data class IcuResponse(
    val data: ArrayList<Icu>
) : BaseResponse()


data class Icu(
    @SerializedName("area_id")
    val area: Int,
    val bed_number: Int,
    @SerializedName("city_id")
    val city: Int,
    val created_at: Any,
    val description: String,
    val description_ar: String,
    val description_fr: String,
    val distance: Double,
    val id: Int,
    val lat: Double,
    val lng: Double,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val rate: String,
    val updated_at: Any
)