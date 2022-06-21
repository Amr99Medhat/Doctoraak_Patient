package com.doctoraak.doctoraakpatient.repository.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfigure {
    //val BASE_URL: String by lazy { "http://v2.doctoraak.com/api/" }
    val BASE_URL: String by lazy { "http://v2.doctoraak.com/api/" }
    val PAYMENT_URL : String by lazy { "https://accept.paymobsolutions.com/api/" }

    internal val mainRetrofit : Retrofit by lazy {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logger)
            .callTimeout(1 , TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    internal val paymentRetrofit: Retrofit by lazy {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logger)
            .callTimeout(1 , TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()

        Retrofit.Builder()
            .baseUrl(PAYMENT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}