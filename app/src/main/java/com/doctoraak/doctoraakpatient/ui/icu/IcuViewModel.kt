package com.doctoraak.doctoraakpatient.ui.icu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.IcuResponse
import com.doctoraak.doctoraakpatient.utils.SingleLiveEvent

class IcuViewModel : ViewModel() {
    val pageNum by lazy { MutableLiveData<Int>(0) }
    val icuResponse: SingleLiveEvent<IcuResponse>
            by lazy { SingleLiveEvent<IcuResponse>() }
    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>(false) }
    val errorMsg: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>("") }
    val errorInt: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>(0) }


    fun getIcus(latt: Double, lang: Double, isIsolationCenter: Boolean) {
        IcuRepository.getIcus(latt,
            lang, isIsolationCenter, pageNum.value!! + 1,
            success = {
                isLoading.value = false
                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!! + 1)

                icuResponse.value = it;
            },
            loading = { if (pageNum.value == 0) isLoading.value = true },
            errorMsg = { isLoading.value = false;errorMsg.value = it },
            error = { isLoading.value = false; errorInt.value = it })
    }

    fun getIcus(
        area: Int, city: Int, isIsolationCenter: Boolean
    ) {

        IcuRepository.getIcus(area,
            city, isIsolationCenter, pageNum.value!! + 1,
            success = {
                icuResponse.value = it;
                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!! + 1)

                isLoading.value = false
            },
            loading = { if (pageNum.value == 0) isLoading.value = true },
            errorMsg = { isLoading.value = false;errorMsg.value = it },
            error = { isLoading.value = false; errorInt.value = it })
    }


    fun initPageNumber() {
        pageNum.value = 0
    }
}