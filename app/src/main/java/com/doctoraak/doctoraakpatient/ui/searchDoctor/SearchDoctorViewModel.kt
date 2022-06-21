package com.doctoraak.doctoraakpatient.ui.searchDoctor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.ClinicFilter
import com.doctoraak.doctoraakpatient.model.ClinicsResponse
import com.doctoraak.doctoraakpatient.model.FavDoctorResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.favouriteDoctor.FavoriteDoctorRepository
import com.doctoraak.doctoraakpatient.utils.SingleLiveEvent

class SearchDoctorViewModel : ViewModel() {
    val pageNum by lazy { MutableLiveData<Int>(0) }
    val clinicsReponse: SingleLiveEvent<ClinicsResponse>
            by lazy { SingleLiveEvent<ClinicsResponse>() }
    val favDoctorResposne: MutableLiveData<FavDoctorResponse>
            by lazy { MutableLiveData<FavDoctorResponse>() }

    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>(false) }
    val errorMsg: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>("") }
    val errorInt: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>(0) }

    fun getClinics(clinicFilter: ClinicFilter)
    {
        if (isLoading.value!!) return

        SearchDoctorRepository.getClinics(clinicFilter, SessionManager.getDoctorType()!!,
            pageNum.value!! + 1,
            success = {
                isLoading.value = false
                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!! + 1)
                clinicsReponse.postValue(it)
                isLoading.value = false
            },
            loading = { if (pageNum.value == 0) isLoading.value = true },
            errorMsg = { isLoading.value = false;errorMsg.value = it },
            error = { isLoading.value = false; errorInt.value = it })
    }

    fun favDoctor(patientId: Int, doctorId: Int, apiToken: String) {
        FavoriteDoctorRepository.favDoctor(patientId,
            doctorId,
            apiToken,
            success = {
                favDoctorResposne.value = it; isLoading.value = false
            },
            loading = { isLoading.value = true },
            errorMsg = { isLoading.value = false },
            error = { isLoading.value = false })
    }

    fun initPageNumber() {
        pageNum.value = 0
    }


}