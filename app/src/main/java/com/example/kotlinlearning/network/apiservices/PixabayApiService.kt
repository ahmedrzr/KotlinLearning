package com.example.kotlinlearning.network.apiservices

import com.example.kotlinlearning.models.remote.pixabay.PixabayResponse
import com.example.kotlinlearning.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {

    @GET("/api/")
    suspend fun getQueryImages(
        @Query("key") key: String = Constants.PIXABAY_KEY,
        @Query("q") q: String = Constants.API_DEFAULT_SEARCH_QUERY1,
        @Query("image_type") imageType: String = "photo"
    ): Response<PixabayResponse>

    @GET("/api/")
    suspend fun getQueryImagesWithPagination(
        @Query("key") key: String = Constants.PIXABAY_KEY,
        @Query("q") q: String = Constants.API_DEFAULT_SEARCH_QUERY1,
        @Query("image_type") imageType: String = "photo",
        @Query("page") page: Int = 0
    ): Response<PixabayResponse>
}