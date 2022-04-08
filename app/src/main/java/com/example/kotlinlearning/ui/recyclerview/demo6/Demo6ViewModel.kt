package com.example.kotlinlearning.ui.recyclerview.demo6

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Resource
import com.example.kotlinlearning.ui.recyclerview.demo2.Demo2ViewModel
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.Constants
import com.example.kotlinlearning.utils.CustomLogging
import kotlinx.coroutines.*

class Demo6ViewModel(private val pixabayRepository: PixabayRepository) : ViewModel() {

    private var pixabayQueryImagesResponse = MutableLiveData<Resource<List<Hit>>>()
    private var _imagesList = ArrayList<Hit>()

    val imagesList = MutableLiveData<ArrayList<Hit>>()
    val pixabayQueryImages get() = pixabayQueryImagesResponse
    private var job: Job? = null
    private val exceptioHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Error Triggered : ${throwable.localizedMessage}")
    }

    private var _queryName = MutableLiveData(Constants.API_DEFAULT_SEARCH_QUERY1)
    val queryName get() = _queryName.value!!


    private var lastQueryString = MutableLiveData<String>()

    private var _totalHits = MutableLiveData<String>()
    val totalHits get() = _totalHits.value!!

    var _page = MutableLiveData(1)
    val page get() = _page.value!!

    private var _isLastPage = MutableLiveData(false)
    var isLastPage = _isLastPage

    private var _isLoading = MutableLiveData(false)
    val isLoading = _isLoading
    var loadingData = ArrayList<Hit>()
    fun queryPixabayApiService(query: String = queryName) {
        if (!_isLoading.value!!) {
            _isLoading.postValue(true)
            if (_isLastPage.value!! && (lastQueryString.value.toString() == query)) {
                _isLoading.postValue(false)
                return
            }
            if (lastQueryString.value != null) {
                if (lastQueryString.value.toString() != query) {
                    _page.value = 1
                    _imagesList.clear()
                    imagesList.postValue(_imagesList)
                } else {
                    _page.value = _page.value!!.toInt() + 1
                    val hit = Hit()
                    _imagesList.add(hit)
                    imagesList.postValue(_imagesList)
                }
            }

            pixabayQueryImagesResponse.postValue(Resource.loading(imagesList.value))
            job = CoroutineScope(Dispatchers.IO + exceptioHandler).launch {
                val response =
                    pixabayRepository.queryPixabayImagesByPagination(
                        query,
                        page.toString()
                    )
                response.let {
                    if (it.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            _totalHits.value = it.body()!!.totalHits.toString()
                            lastQueryString.value = query
                            if (page > 1) {
                                delay(5000)
                                _imagesList = imagesList.value!!
                                _imagesList.removeLast()
                                _imagesList.addAll(it.body()!!.hits as ArrayList<Hit>)
                                imagesList.value = _imagesList
                            } else {
                                _imagesList.addAll(it.body()!!.hits as ArrayList<Hit>)
                                imagesList.value = _imagesList
                            }
                            _isLastPage.postValue(imagesList.value!!.size == totalHits.toInt())
                            pixabayQueryImagesResponse.postValue(Resource.success(imagesList.value as List<Hit>))
                            _isLoading.postValue(false)
                        }
                    } else {
                        _isLoading.postValue(false)
                        onError(it.message())
                    }
                }
            }
        } else {
// TODO
        }
    }

    private fun onError(errorMsg: String) {
        // _isLoading.value = false
        pixabayQueryImagesResponse.postValue(Resource.error(errorMsg, null))
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}