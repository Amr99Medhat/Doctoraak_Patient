package com.doctoraak.doctoraakpatient.ui.lap


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.LabFilter
import com.doctoraak.doctoraakpatient.model.LabResponse
import com.doctoraak.doctoraakpatient.utils.SingleLiveEvent

class LapViewModel : ViewModel() {

    val labsReponse: SingleLiveEvent<LabResponse>
            by lazy { SingleLiveEvent<LabResponse>() }
    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>(false) }
    val errorMsg: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>("") }
    val errorInt: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>(0) }
    val pageNum by lazy { MutableLiveData<Int>(0) }


    fun filterLabs(filter: LabFilter)
    {
        LapRepository.filterLaps(filter, pageNum.value!!+1,
            success = {
                isLoading.value = false

                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!!+1)

                labsReponse.value = it;
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