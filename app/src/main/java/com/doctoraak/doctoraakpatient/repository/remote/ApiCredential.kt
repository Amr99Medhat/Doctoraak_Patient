package com.doctoraak.doctoraakpatient.repository.remote

import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.ResendSMSCodeResponse
import com.doctoraak.doctoraakpatient.model.User
import com.doctoraak.doctoraakpatient.model.UserResponse
import com.doctoraak.doctoraakpatient.utils.start
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

object ApiCredential {
    fun logIn(phone: String, password: String, callback: BaseResponseListener<UserResponse>) {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<UserResponse> = service.logIn(phone, password)
        call.start(callback)
    }

    fun register(user: User, callback: BaseResponseListener<UserResponse>) {
        val image_file = File(user.photo)
        val imageData = MultipartBody.Part.createFormData("photo", image_file.name,
                createRequestBodyFile(image_file))

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<UserResponse> = if (user.insuranceId != -1) {
        service.register(phone = createRequestBody(user.phone)
            , gender = createRequestBody(user.gender)
            , password = createRequestBody(user.password)
            , email = createRequestBody(user.email)
            , name = createRequestBody(user.name)
            , address = createRequestBody(user.address)
            , birthdate = createRequestBody(user.birthdate)
            , photo = if (user.photo.isNotBlank() && image_file.exists()) imageData else null
            , insuranceId = createRequestBody(user.insuranceId.toString())
            ,insurance_code_id = createRequestBody(user.insuranceCode.toString()))
    }else{
            service.register(phone = createRequestBody(user.phone)
                , gender = createRequestBody(user.gender)
                , password = createRequestBody(user.password)
                , email = createRequestBody(user.email)
                , name = createRequestBody(user.name)
                , address = createRequestBody(user.address)
                , birthdate = createRequestBody(user.birthdate)
                , photo = if (user.photo.isNotBlank()
                    && image_file.exists()) imageData else null
                )
        }

        call.start(callback)
    }

    fun forgetPasswordStep_1(phone: String, callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.forgetPassword(phone)
        call.start(callback)
    }

    fun forgetPasswordUpdatePasswordStep_2(phone: String, password: String, new_password: String
                                           , callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.forgetPasswordUpdatePassword(phone, password, new_password)
        call.start(callback)
    }

    fun resendSmsCode(userID : Int, callback: BaseResponseListener<ResendSMSCodeResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<ResendSMSCodeResponse> = service.resendSmsCode(userID)
        call.start(callback)
    }

    fun activeAccount(user_id: Long, sms_code: String, callback: BaseResponseListener<UserResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<UserResponse> = service.activeAccount(user_id, sms_code)
        call.start(callback)
    }

    private fun createRequestBody(data: String)
            = RequestBody.create(MediaType.parse("text/plain"), data)

    private fun createRequestBodyFile(file: File)
            = RequestBody.create(MediaType.parse("image/*"), file)

}


