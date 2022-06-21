package com.doctoraak.doctoraakpatient.ui.splash

import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.remote.ApiLab
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.ApiRadiology
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object SplashRepository {

    fun getCities( success: (CityResponse)->Unit, loading: ()->Unit
                       , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getCities( object : BaseResponseListener<CityResponse>
        {
            override fun onSuccess(model: CityResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })

    fun getAreas( success: (AreaResponse)->Unit
                   , loading: ()->Unit
                   , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getAreas( object : BaseResponseListener<AreaResponse>
        {
            override fun onSuccess(model: AreaResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })


    fun getAnaylsis( success: (AnalysisResponse)->Unit
                  , loading: ()->Unit
                  , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getAnaylsis( object : BaseResponseListener<AnalysisResponse>
        {
            override fun onSuccess(model: AnalysisResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })


    fun getMedicines( success: (MedicinesResponse)->Unit
                   , loading: ()->Unit
                   , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getMedicines( object : BaseResponseListener<MedicinesResponse>
        {
            override fun onSuccess(model: MedicinesResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })

    fun getInsurrance( success: (InsuranceResponse)->Unit
                      , loading: ()->Unit
                      , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getInsurrance( object : BaseResponseListener<InsuranceResponse>
        {
            override fun onSuccess(model: InsuranceResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })

    fun getMedicinesType( success: (MedicinesTypeResponse)->Unit
                      , loading: ()->Unit
                      , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getMedicinesType( object : BaseResponseListener<MedicinesTypeResponse>
        {
            override fun onSuccess(model: MedicinesTypeResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })

    fun getRays( success: (RaysResponse)->Unit
                          , loading: ()->Unit
                          , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getRays( object : BaseResponseListener<RaysResponse>
        {
            override fun onSuccess(model: RaysResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorMsgId: Int)
            {
                error(errorMsgId)
            }
        })

    fun getAllLabs(
        success: (AllLabsResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiLab.getAllLabs(object : BaseResponseListener<AllLabsResponse> {
            override fun onSuccess(model: AllLabsResponse) {
                success(model)
            }

            override fun onLoading() {
                loading()
            }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })

    fun getAllRadiologies(
        success: (AllRadiologiesResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiRadiology.getAllRadiologies(object : BaseResponseListener<AllRadiologiesResponse> {
            override fun onSuccess(model: AllRadiologiesResponse) {
                success(model)
            }

            override fun onLoading() {
                loading()
            }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })

    fun getContactInfo(
        success: (ContactUsResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiOthers.getContactInfo(object : BaseResponseListener<ContactUsResponse> {
            override fun onSuccess(model: ContactUsResponse) {
                success(model)
            }

            override fun onLoading() {
                loading()
            }

            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }

            override fun onError(errorMsgId: Int) {
                error(errorMsgId)
            }
        })

}