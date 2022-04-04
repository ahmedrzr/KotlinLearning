package com.example.kotlinlearning.network

import com.example.kotlinlearning.network.apiservices.PixabayApiService
import com.example.kotlinlearning.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val logIntercepter by lazy {
            HttpLoggingInterceptor()
        }
        private val httpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(logIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY))

        }
        private val retrofit: Retrofit by lazy {
            Retrofit
                .Builder()
                .baseUrl(Constants.PIXABAY_URL)
                .addConverterFactory(GsonConverterFactory.create())
               .client(httpClient.build())
                .build()
        }

        val queryPixabayImages: PixabayApiService = retrofit.create(PixabayApiService::class.java)

    }
}