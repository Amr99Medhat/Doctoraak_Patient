package com.doctoraak.doctoraakpatient.ui.radiologyOrder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.RadiologyOrderRequest
import com.doctoraak.doctoraakpatient.model.RadiologyOrderResponse

class RadiologyOrderViewModel : ViewModel() {

    val radiologyOrdeResponse: MutableLiveData<RadiologyOrderResponse>
            by lazy { MutableLiveData<RadiologyOrderResponse>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun createRadiologyOrder(request : RadiologyOrderRequest) {

        RadiologyOrderRepository.createRadiologyOrder(request, success = {
            radiologyOrdeResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }



}