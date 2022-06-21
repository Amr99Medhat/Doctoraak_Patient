package com.doctoraak.doctoraakpatient.model

data class AnalysisResponse(
    val data: ArrayList<Anaylsis>
) : BaseResponse()

data class Anaylsis(
    val created_at: String,
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_fr: String,
    val updated_at: String
)