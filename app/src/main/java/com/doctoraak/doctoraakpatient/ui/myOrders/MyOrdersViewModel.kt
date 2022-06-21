package com.doctoraak.doctoraakpatient.ui.myOrders


import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.RatingResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.LabOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.PharmacyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.RadiologyOrdersListResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.ReservationsResponse
import com.doctoraak.doctoraakpatient.utils.SingleLiveEvent

class MyOrdersViewModel : ViewModel()
{
    val rateDoctorResposne: SingleLiveEvent<RatingResponse>
            by lazy { SingleLiveEvent<RatingResponse>() }
    val cancelOrderResponse: SingleLiveEvent<BaseResponse>
            by lazy { SingleLiveEvent<BaseResponse>() }
    val doctorReservation: SingleLiveEvent<ReservationsResponse> by lazy { SingleLiveEvent<ReservationsResponse>() }
    val pharmacyReservation: SingleLiveEvent<PharmacyOrdersListResponse> by lazy { SingleLiveEvent<PharmacyOrdersListResponse>() }
    val labReservation: SingleLiveEvent<LabOrdersListResponse> by lazy { SingleLiveEvent<LabOrdersListResponse>() }
    val radiologyReservation: SingleLiveEvent<RadiologyOrdersListResponse> by lazy { SingleLiveEvent<RadiologyOrdersListResponse>() }


    fun getMyOrdersLab(patientId: Int , apiToken: String,
                       loading: (Boolean) -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit
    )
    {
        MyOrdersRepository.getMyOrdersLab(patientId , apiToken,success = {
            labReservation.value = it
            loading(false)
        }, loading = {loading(true)}
            , errorMsg = {loading(false);  errorMsg(it)}
            , error = {loading(false); error(it)} )
    }

    fun getMyOrdersPharmacy(patientId: Int , apiToken: String ,
                            loading: (Boolean) -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit)
    {
        MyOrdersRepository.getMyOrdersPharmacy(patientId , apiToken,success = {
            pharmacyReservation.value = it
            loading(false)
        }, loading = {loading(true) }
            , errorMsg = {loading(false); errorMsg(it)}
            , error = {loading(false); error(it)} )
    }

    fun getMyOrdersRadiology(patientId: Int , apiToken: String
                             , loading: (Boolean) -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit)
    {
        MyOrdersRepository.getMyOrdersRadiology(patientId , apiToken,success = {
            radiologyReservation.value = it
            loading(false)
        }, loading = {loading(true) }
            , errorMsg = {loading(false); errorMsg(it)}
            , error = {loading(false); error(it)} )
    }

    fun getReservations(patientId: Int, apiToken: String
    , loading: (Boolean) -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit) {

        MyOrdersRepository.getReservations(patientId, apiToken, success = {
            doctorReservation.value = it; loading(false)
        }, loading = { loading(true) }
            , errorMsg = { loading(false); errorMsg(it) }
            , error = { loading(false);  error(it)})
    }

    fun rateDoctor(patientId: Int,doctorId: Int ,rate : Int, apiToken: String , type : String
                   , loading: (Boolean) -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit)
    {
        MyOrdersRepository.rateDoctor(patientId,doctorId ,rate, apiToken,type,success = {
            rateDoctorResposne.value = it ; loading(false)
        }, loading = {loading(true) }
            , errorMsg = {loading(false);errorMsg(it) }
            , error = {loading(false); error(it)} )
    }

    fun cancelOrder(api_token: String,patient_id: Int, order_id: Int, order_type: String, message: String
                    , loading: (Boolean) -> Unit, errorMsg: (String) -> Unit, error: (Int) -> Unit) {

        MyOrdersRepository.cancelOrder(api_token,patient_id,order_id,order_type,message
            , success = {
                cancelOrderResponse.value = it; loading(false)
            }, loading = { loading(true) }
            , errorMsg = { loading(false) ;errorMsg(it)  }
            , error = { loading(false); error(it)})
    }
}