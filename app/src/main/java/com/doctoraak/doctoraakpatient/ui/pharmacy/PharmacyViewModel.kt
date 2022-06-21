package com.doctoraak.doctoraakpatient.ui.pharmacy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.MedicineOrderRequest
import com.doctoraak.doctoraakpatient.model.PharmacyFilterRequest
import com.doctoraak.doctoraakpatient.model.PharmacyFilterResponse
import com.doctoraak.doctoraakpatient.model.PharmacyOrderResponse

class PharmacyViewModel : ViewModel() {

    val pageNum: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
    val pharmacyOrdeResponse: MutableLiveData<PharmacyOrderResponse>
            by lazy { MutableLiveData<PharmacyOrderResponse>() }
    val pharmacyFilterResponse: MutableLiveData<PharmacyFilterResponse>
            by lazy { MutableLiveData<PharmacyFilterResponse>() }
    val AllPharmacyResponse: MutableLiveData<PharmacyFilterResponse>
            by lazy { MutableLiveData<PharmacyFilterResponse>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isLoadingPharmacies: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }


    fun createPharmacyOrder(request: MedicineOrderRequest, s: String) {

        PharmacyRepository.createPharmacyOrder(request,
            s,
            success = {
                pharmacyOrdeResponse.value = it; isLoading.value = false
            },
            loading = { isLoading.value = true },
            errorMsg = { isLoading.value = false;errorMsg.value = it },
            error = { isLoading.value = false; errorInt.value = it })
    }

    fun getFilteredPharmacies(pharmacyFilterRequest: PharmacyFilterRequest,
                              onLoading: (Boolean) -> Unit,
                              errorMsg: (String) -> Unit,
                              error: (Int) -> Unit) {
        PharmacyRepository.getFilteredPharmacies(pharmacyFilterRequest,
            pageNum = pageNum.value!! + 1,
            success = {
                if (!it.data.isNullOrEmpty())
                    pageNum.postValue(pageNum.value!!+1)
                pharmacyFilterResponse.value = it;
                onLoading(false)
            },
            loading = { if (pageNum.value == 0) onLoading(true) },
            errorMsg = { onLoading(false); errorMsg(it) },
            error = { onLoading(false); error(it) })
    }

    fun getAllPharmacies(errorMsg: (String) -> Unit,
                         error: (Int) -> Unit) {
        PharmacyRepository.getAllPharmacies(success = {
            AllPharmacyResponse.value = it; isLoadingPharmacies.value = false
        }, loading = { isLoadingPharmacies.value = true },
            errorMsg = { isLoadingPharmacies.value = false; errorMsg(it) }, error = { isLoadingPharmacies.value = false; error(it) })
    }

}