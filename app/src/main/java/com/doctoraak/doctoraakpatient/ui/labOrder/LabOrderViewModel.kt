package com.doctoraak.doctoraakpatient.ui.labOrder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.LabOrderRequest
import com.doctoraak.doctoraakpatient.model.LabOrderResponse

class LabOrderViewModel : ViewModel() {

    val labOrdeResponse: MutableLiveData<LabOrderResponse>
            by lazy { MutableLiveData<LabOrderResponse>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun createLabOrder(request : LabOrderRequest) {

        LabOrderRepository.createLabOrder(request, success = {
            labOrdeResponse.value = it; isLoading.value = false
        }, loading = { isLoading.value = true }
            , errorMsg = { isLoading.value = false ;errorMsg.value = it }
            , error = { isLoading.value = false ; errorInt.value = it})
    }

}