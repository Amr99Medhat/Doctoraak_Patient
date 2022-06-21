package com.doctoraak.doctoraakpatient.repository.remote

interface BaseResponseListener<T>
{
    fun onSuccess(model: T)
    fun onLoading()
    fun onErrorMsg(errorMsg: String)
    /**
     * @param errorMsgId is the string id which you can get it like: ('getString(errorMsgId)')
     * called when the request is failed
     * */
    fun onError(errorMsgId: Int)
}