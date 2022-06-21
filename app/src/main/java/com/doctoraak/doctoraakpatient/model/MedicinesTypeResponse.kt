package com.doctoraak.doctoraakpatient.model

data class MedicinesTypeResponse(
    val data: ArrayList<MedicineType>
):BaseResponse()

data class MedicineType(
val created_at: String,
val id: Int,
val name: String,
val name_ar: String,
val name_fr: String,
val updated_at: String
)