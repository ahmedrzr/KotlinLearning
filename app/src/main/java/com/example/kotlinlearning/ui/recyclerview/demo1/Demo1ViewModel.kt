package com.example.kotlinlearning.ui.recyclerview.demo1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Resource
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import kotlinx.coroutines.*

class Demo1ViewModel(private val pixabayRepository: PixabayRepository) : ViewModel() {

    private var pixabayQueryImagesResponse = MutableLiveData<Resource<List<Hit>>>()
    val pixabayQueryImages get() = pixabayQueryImagesResponse
    private var job: Job? = null
    private val exceptioHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Error Triggered : ${throwable.localizedMessage}")
    }

    fun queryPixabayApiService(query: String) {
        pixabayQueryImagesResponse.postValue(Resource.loading(null))
        job = CoroutineScope(Dispatchers.IO + exceptioHandler).launch {
            val response = pixabayRepository.queryPixabayImages(query)
            response.let {
                if (it.isSuccessful) {
                    pixabayQueryImagesResponse.postValue(Resource.success(it.body()?.hits))
                } else {
                    onError(it.message())
                }
            }
        }
    }

    private fun onError(errorMsg: String) {
        pixabayQueryImagesResponse.postValue(Resource.error(errorMsg, null))
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}