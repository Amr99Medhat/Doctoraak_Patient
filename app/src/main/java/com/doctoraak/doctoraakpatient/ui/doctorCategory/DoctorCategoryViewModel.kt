package com.doctoraak.doctoraakpatient.ui.doctorCategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.DoctorCategoryResponse

class DoctorCategoryViewModel : ViewModel() {

    val cate: MutableLiveData<DoctorCategoryResponse>
            by lazy { MutableLiveData<DoctorCategoryResponse>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun getCategories()
    {

        DoctorCategoryRepository.getCategories(success = {
            cate.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }


}