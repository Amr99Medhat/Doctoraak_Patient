package com.doctoraak.doctoraakpatient.model

import com.google.gson.annotations.SerializedName

open class BaseResponse
{
    @SerializedName("status")
    var status: Int = 0

    @SerializedName("message")
    var message: String? = ""

    @SerializedName("message_en")
    var message_en: String? = ""
    
}