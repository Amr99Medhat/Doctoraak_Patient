package com.doctoraak.doctoraakpatient.model

data class CityResponse (val data: ArrayList<City>): BaseResponse()

data class City(
    val created_at: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)