package com.doctoraak.doctoraakpatient.model

data class DoctorCategoryResponse (
    val data : ArrayList<Category>
): BaseResponse()

data class Category(
    val created_at: String,
    val icon: String,
    val url: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)