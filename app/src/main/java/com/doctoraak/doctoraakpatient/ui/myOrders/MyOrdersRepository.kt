package com.doctoraak.doctoraakpatient.ui.myOrders

import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.RatingResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.LabOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.PharmacyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.RadiologyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.ReservationsResponse
import com.doctoraak.doctoraakpatient.repository.remote.ApiDoctor
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener

object MyOrdersRepository
{
    fun getMyOrdersPharmacy(patientId: Int , apiToken: String
                    , success: (PharmacyOrdersListResponse)->Unit
                    , loading: ()->Unit
                    , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getMyOrdersPharmacy(apiToken, patientId
            , object : BaseResponseListener<PharmacyOrdersListResponse>
            {
                override fun onSuccess(model: PharmacyOrdersListResponse) {
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

    fun getMyOrdersLab(patientId: Int , apiToken: String
                    , success: (LabOrdersListResponse)->Unit
                    , loading: ()->Unit
                    , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getMyOrdersLab(apiToken, patientId
            , object : BaseResponseListener<LabOrdersListResponse>
            {
                override fun onSuccess(model: LabOrdersListResponse) {
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

    fun getMyOrdersRadiology(patientId: Int , apiToken: String
                       , success: (RadiologyOrdersListResponse)->Unit
                       , loading: ()->Unit
                       , errorMsg: (String)->Unit, error: (Int)->Unit) =
        ApiOthers.getMyOrdersRadiology(apiToken, patientId
            , object : BaseResponseListener<RadiologyOrdersListResponse>
            {
                override fun onSuccess(model: RadiologyOrdersListResponse) {
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

    //doctor reservations
    fun getReservations(patientId: Int, apiToken: String, success: (ReservationsResponse) -> Unit
                        , loading: () -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit) =
        ApiOthers.getReservations(patientId, apiToken,
            object : BaseResponseListener<ReservationsResponse> {
                override fun onSuccess(model: ReservationsResponse) {
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

    fun rateDoctor(
        patientId: Int, doctorId: Int, rate: Int, apiToken: String, type: String
        , success: (RatingResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) =
        ApiDoctor.rateDoctor(patientId, doctorId, rate, apiToken, type,
            object : BaseResponseListener<RatingResponse> {
                override fun onSuccess(model: RatingResponse) {
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

    fun cancelOrder(api_token: String, patient_id: Int, order_id: Int, order_type: String, message: String
                    , success: (BaseResponse) -> Unit, loading: () -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit
    ) = ApiOthers.cancelOrder(api_token,patient_id, order_id, order_type,message
        , object : BaseResponseListener<BaseResponse> {
            override fun onSuccess(model: BaseResponse) {
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
}