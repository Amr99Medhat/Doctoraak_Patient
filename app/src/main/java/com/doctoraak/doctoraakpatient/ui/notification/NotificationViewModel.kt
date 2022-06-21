package com.doctoraak.doctoraakpatient.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.NotificationsResponse

class NotificationViewModel : ViewModel() {

    val notificationsResponse: MutableLiveData<NotificationsResponse>
            by lazy { MutableLiveData<NotificationsResponse>() }
    val cancelOrderResponse: MutableLiveData<BaseResponse>
            by lazy { MutableLiveData<BaseResponse>() }

    val removeNotification: MutableLiveData<BaseResponse>
            by lazy { MutableLiveData<BaseResponse>() }

    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun getNotifications(userId: Int, userType: String, api_token: String) {

        NotificationRepository.getNotifications(userId,userType,api_token
            , success = {
            notificationsResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

    fun cancelOrder(api_token: String,patient_id: Int, order_id: Int, order_type: String, message: String) {

        NotificationRepository.cancelOrder(api_token,patient_id,order_id,order_type,message
            , success = {
                cancelOrderResponse.value = it; isLoading.value = false
            }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

    fun removeNotifications(notificationId: Int) {

        NotificationRepository.removeNotifications(notificationId
            , success = {
                removeNotification.setValue(it); isLoading.setValue(false)
            }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

}