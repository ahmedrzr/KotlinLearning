package com.example.kotlinlearning.ui.recyclerview.repository


import com.example.kotlinlearning.models.remote.pixabay.PixabayResponse
import com.example.kotlinlearning.network.RetrofitInstance
import retrofit2.Response

class PixabayRepository {

    suspend fun queryPixabayImages(query: String): Response<PixabayResponse> =
        RetrofitInstance
            .queryPixabayImages
            .getQueryImages(q = query)

    suspend fun queryPixabayImagesByPagination(
        query: String,
        pageNumber: Int
    ): Response<PixabayResponse> =
        RetrofitInstance
            .queryPixabayImages
            .getQueryImagesWithPagination(q = query, page = pageNumber)

}