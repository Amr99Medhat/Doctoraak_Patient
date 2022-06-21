package com.doctoraak.doctoraakpatient.model

data class AreaResponse(
    val data: List<Area>
) : BaseResponse()

data class Area(
    val created_at: String,
    val id: Int,
    val name: String,
    val city_id: Int,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)