package com.example.yelprestaurants.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitApiCall {


    //region Retrofit object
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    private fun client() : OkHttpClient {
        val clientBuilder = OkHttpClient().newBuilder()
            .addNetworkInterceptor{
                val headers: MutableMap<String, String> = hashMapOf()
                with(headers){
                    put("Authorization", "Bearer ${Constants.API_KEY}")
                }
                val builder = it.request().newBuilder()
                for ((key, value) in headers) {
                    when {
                        value.isNotEmpty() -> builder.addHeader(key, value)
                    }
                }
                val request = builder.build()
                return@addNetworkInterceptor it.proceed(request)
            }
            .readTimeout(0, TimeUnit.NANOSECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            clientBuilder.addNetworkInterceptor(HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY))

        return clientBuilder.build()
    }


    companion object{
        private val retrofitApiCall = RetrofitApiCall()

        var apiService = retrofitApiCall.retrofit.create(ApiService::class.java)
    }
}