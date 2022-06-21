package com.doctoraak.doctoraakpatient.repository.remote

import android.util.Log
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.PaymentErrorDetails
import com.doctoraak.doctoraakpatient.utils.getMsg
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketException

class BaseCallBack<T>(val listener: BaseResponseListener<T>) : Callback<T>
{

    override fun onResponse(call: Call<T>, response: Response<T>)
    {
        Log.d("logApi", "//////////////////   ${call.request().url()}   /////////////////")
        Log.d("logApi", "onResponse request body= " + call.request().body().toString())
        Log.d("logApi", "onResponse code= " + response.code())
        Log.d("logApi", "onResponse data= " + Gson().toJson(response.body()))

        when (response.code())
        {
            200 -> {
                response.body().let {

                    if (it as? BaseResponse != null) {
                        Log.d("logApi", "onResponse status= " + (it as BaseResponse).status)
                        if ((it as BaseResponse).status == 1)
                            listener.onSuccess(it)
                        else {
                            Log.d("logApi", "onResponse msg= " + (it as BaseResponse).getMsg())
                            listener.onErrorMsg((it as BaseResponse).getMsg())
                        }
                    } else {
                        listener.onSuccess(it!!)
                    }
                }
            }
            201 -> {
                response.body()?.let {
                    listener.onSuccess(it)
                }
            }
            403 -> {
                response.body()?.let {
                    listener.onErrorMsg((it as PaymentErrorDetails).detail)
                }
            }
            401 -> listener.onError(R.string.unauthorized_user)
            500 -> listener.onError(R.string.server_error)
            else -> {
                listener.onError(R.string.unexpected_error)
            }
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable)
    {
        Log.d("logApi", "//////////////////   ${call.request().url()}   /////////////////")
        Log.e("logApi", "onFailed, " + t.message, t.cause)
        listener.onError(t.handleError())
    }

}

private fun Throwable.handleError(): Int = when (this) {

    is HttpException -> {
        when (code()) {
            400 -> R.string.bad_request
            401 -> R.string.unauthorized_user
            404 -> R.string.request_not_found
            500 -> R.string.server_error
            else -> R.string.unexpected_error
        }
    }
    is SocketException -> R.string.not_connected_to_network
    is IOException -> R.string.IOException
    else -> {
        R.string.unexpected_error
    }
}
