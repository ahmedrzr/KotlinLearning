package com.example.kotlinlearning.network.apihelpers

import android.util.Log
import com.example.kotlinlearning.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.HttpMethod
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName

private val TAG = CustomInterceptor::class.java.simpleName

class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val url = if (originalRequest.url.toString()
                .contains(Constants.PIXABAY_URL)
        ) {
            originalRequest.url
                .newBuilder()
                .addQueryParameter("per_page", "25")
                .build()
        } else originalRequest.url
        val request = chain.request()
            .newBuilder().url(url).build()
        return chain.proceed(request)
    }
}