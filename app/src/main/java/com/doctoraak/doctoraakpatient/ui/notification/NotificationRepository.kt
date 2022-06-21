package com.doctoraak.doctoraakpatient.ui.notification

import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.NotificationsResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener


object NotificationRepository {

    fun getNotifications(
        userId: Int, userType: String, api_token: String
        , success: (NotificationsResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) = ApiOthers.getNotifications(userId, userType, api_token
            , object : BaseResponseListener<NotificationsResponse> {
                override fun onSuccess(model: NotificationsResponse) {
                    success(model)
                }
                override fun onLoading()
                {
                    loading()
                }
                override fun onErrorMsg(errorMsg: String) {
                    errorMsg(errorMsg)
                }
                override fun onError(errorMsgId: Int)
                {
                    error(errorMsgId)
                }
            })


    fun cancelOrder(api_token: String,patient_id: Int, order_id: Int, order_type: String, message: String
        , success: (BaseResponse) -> Unit, loading: () -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) = ApiOthers.cancelOrder(api_token,patient_id, order_id, order_type,message
        , object : BaseResponseListener<BaseResponse> {
            override fun onSuccess(model: BaseResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })

    fun removeNotifications(notificationId: Int
                            , success: (BaseResponse) -> Unit
                            , loading: () -> Unit
                            , errorMsg: (String) -> Unit, error: (Int)->Unit) = ApiOthers.removeNotifications(notificationId
        , object : BaseResponseListener<BaseResponse> {
            override fun onSuccess(model: BaseResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int) = error(errorMsgId)
        })
}