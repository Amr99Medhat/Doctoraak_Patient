package com.doctoraak.doctoraakpatient.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.*

class SplashViewModel : ViewModel() {

    val allLabsResponse: MutableLiveData<AllLabsResponse>
            by lazy { MutableLiveData<AllLabsResponse>() }
    val allRadiologiesResponse: MutableLiveData<AllRadiologiesResponse>
            by lazy { MutableLiveData<AllRadiologiesResponse>() }

    val citiesResponse: MutableLiveData<CityResponse>
            by lazy { MutableLiveData<CityResponse>() }
    val areasResponse: MutableLiveData<AreaResponse>
            by lazy { MutableLiveData<AreaResponse>() }

    val insurranceResponse: MutableLiveData<InsuranceResponse>
            by lazy { MutableLiveData<InsuranceResponse>() }

    val mediResponse: MutableLiveData<MedicinesResponse>
            by lazy { MutableLiveData<MedicinesResponse>() }
    val mediTypeResponse: MutableLiveData<MedicinesTypeResponse>
            by lazy { MutableLiveData<MedicinesTypeResponse>() }

    val raysResponse: MutableLiveData<RaysResponse>
            by lazy { MutableLiveData<RaysResponse>() }

    val anaylsisResponse: MutableLiveData<AnalysisResponse>
            by lazy { MutableLiveData<AnalysisResponse>() }

    val contactInfoResponse: MutableLiveData<ContactUsResponse>
            by lazy { MutableLiveData<ContactUsResponse>() }

    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val errorMsg: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val errorInt: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }

    fun getCities()
    {
        SplashRepository.getCities(success = {
            citiesResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false ; errorMsg.value = it}
            , error = {isLoading.value = false ; errorInt.value = it} )
    }

    fun getAreas()
    {
        SplashRepository.getAreas(success = {
            areasResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }

    fun getMedicines()
    {
        SplashRepository.getMedicines(success = {
            mediResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false; errorMsg.value = it} , error = {isLoading.value = false ; ; errorInt.value = it} )
    }

    fun getInsurrance()
    {
        SplashRepository.getInsurrance(success = {
            insurranceResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false; errorMsg.value = it}
            , error = {isLoading.value = false ; ; errorInt.value = it} )
    }

    fun getMedicinesType()
    {
        SplashRepository.getMedicinesType(success = {
            mediTypeResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false; errorMsg.value = it} , error = {isLoading.value = false ; ; errorInt.value = it} )
    }

    fun getRays()
    {
        SplashRepository.getRays(success = {
            raysResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false; errorMsg.value = it} , error = {isLoading.value = false ; ; errorInt.value = it} )
    }

    fun getAnaylsis()
    {
        SplashRepository.getAnaylsis(success = {
            anaylsisResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false; errorMsg.value = it} , error = {isLoading.value = false ; ; errorInt.value = it} )
    }

    fun getAllLabs()
    {
        SplashRepository.getAllLabs(success = {
            allLabsResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }

    fun getAllRadiologies()
    {
        SplashRepository.getAllRadiologies(success = {
            allRadiologiesResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }

    fun getContactInfo()
    {
        SplashRepository.getContactInfo(success = {
            contactInfoResponse.value = it ; isLoading.value = false
        }, loading = {isLoading.value = true }
            , errorMsg = {isLoading.value = false} , error = {isLoading.value = false} )
    }
}