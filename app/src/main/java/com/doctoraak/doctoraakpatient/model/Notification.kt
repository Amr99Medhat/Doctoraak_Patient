package com.doctoraak.doctoraakpatient.model

data class NotificationsResponse(
    val data: ArrayList<NotificationInfo>
) : BaseResponse()

data class NotificationInfo(
    val created_at: String,
    val icon: String,
    val id: Int,
    var order: Any?= null,
    val message_ar: String,
    val message_en: String,
    val order_id: String,
    val seen: Int,
    val sent: Int,
    val title_ar: String,
    val title_en: String,
    val updated_at: String,
    val user_id: Int,
    val user_type: String
)