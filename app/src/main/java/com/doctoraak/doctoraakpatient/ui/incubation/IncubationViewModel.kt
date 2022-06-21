package com.doctoraak.doctoraakpatient.ui.incubation


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.IncubationResponse
import com.doctoraak.doctoraakpatient.utils.SingleLiveEvent

class IncubationViewModel : ViewModel() {

    val incubations: SingleLiveEvent<IncubationResponse>
            by lazy { SingleLiveEvent<IncubationResponse>() }
    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>(false) }
    val errorMsg: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>("") }
    val errorInt: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>(0) }
    val pageNum by lazy { MutableLiveData<Int>(0) }


    fun getIncubations(latt: Double, lang: Double) {

        IncubationRepository.getIncubation(latt, lang,
            pageNum.value!! + 1,
            success = {
                isLoading.value = false
                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!! + 1)
                incubations.value = it;
            },
            loading = { if (pageNum.value == 0) isLoading.value = true },
            errorMsg = { isLoading.value = false;errorMsg.value = it },
            error = { isLoading.value = false; errorInt.value = it })
    }

    fun getIncubations(area: Int, city: Int) {

        IncubationRepository.getIncubation(area,
            city, pageNum.value!! + 1,
            success = {
                isLoading.value = false

                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!! + 1)

                incubations.value = it;
            },
            loading = { if (pageNum.value == 0) isLoading.value = true },
            errorMsg = { isLoading.value = false;errorMsg.value = it },
            error = { isLoading.value = false; errorInt.value = it })
    }


    fun initPageNumber() {
        pageNum.value = 0
    }

}