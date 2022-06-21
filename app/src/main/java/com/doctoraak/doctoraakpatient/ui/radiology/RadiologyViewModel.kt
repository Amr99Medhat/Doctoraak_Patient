package com.doctoraak.doctoraakpatient.ui.radiology


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.RadiologyFilter
import com.doctoraak.doctoraakpatient.model.RadiologyResponse
import com.doctoraak.doctoraakpatient.utils.SingleLiveEvent
import kotlin.math.log

class RadiologyViewModel : ViewModel() {
    val radiologyReponse: SingleLiveEvent<RadiologyResponse>
            by lazy { SingleLiveEvent<RadiologyResponse>() }
    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>(false) }
    val errorMsg: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>("") }
    val errorInt: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>(0) }
    val pageNum by lazy { MutableLiveData<Int>(0) }


    fun filterRadiologies(filter: RadiologyFilter) {
        RadiologyRepository.filterRadiologies(filter,
            pageNum.value!! + 1,
            success = {
                isLoading.value = false

                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!! + 1)

                radiologyReponse.value = it
            },
            loading = { isLoading.value = true },
            errorMsg = { isLoading.value = false;errorMsg.value = it },
            error = { isLoading.value = false; errorInt.value = it })
    }

    fun initPageNumber()
    {
        pageNum.value = 0
    }


}