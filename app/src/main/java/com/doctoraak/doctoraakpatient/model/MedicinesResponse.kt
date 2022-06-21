package com.doctoraak.doctoraakpatient.model

data class MedicinesResponse(
    val data: ArrayList<Medicine>
) :BaseResponse()

data class Medicine(
    val created_at: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)
