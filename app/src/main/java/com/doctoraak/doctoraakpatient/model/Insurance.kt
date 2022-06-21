package com.doctoraak.doctoraakpatient.model

import com.google.gson.annotations.SerializedName

data class Insurance(
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("name_ar")
    var name_ar: String = "",
    @SerializedName("name_fr")
    var name_fr: String = "",
    @SerializedName("created_at")
    var created_at: String = "",
    @SerializedName("updated_at")
    var updated_at: String = ""
)

data class InsuranceResponse(
    @SerializedName("data")
    var data: ArrayList<Insurance>?
) : BaseResponse()