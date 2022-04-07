package com.example.kotlinlearning.ui.recyclerview.demo2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Resource
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.Constants
import com.example.kotlinlearning.utils.CustomLogging
import kotlinx.coroutines.*
import java.lang.Exception

class Demo2ViewModel(private val pixabayRepository: PixabayRepository) : ViewModel() {

    private var pixabayQueryImagesResponse = MutableLiveData<Resource<List<Hit>>>()
    private var imagesList = MutableLiveData<ArrayList<Hit>>()
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

    fun queryPixabayApiService(query: String = queryName) {
        _isLoading.postValue(true)
        if (_isLastPage.value!! && (lastQueryString.value.toString() == query)) {
            _isLoading.postValue(false)
            return
        }
        if (lastQueryString.value != null) {
            if (lastQueryString.value.toString() != query) {
                CustomLogging.normalLog(Demo2ViewModel::class.java, "NEW SEARCH QUERY")
                _page.value = 1
                imagesList.value!!.clear()
            }
        }
        pixabayQueryImagesResponse.postValue(Resource.loading(null))
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
                            val list = imagesList.value!!
                            list.addAll(it.body()!!.hits as ArrayList<Hit>)
                            imagesList.value = list
                        } else {
                            imagesList.value = (it.body()!!.hits as ArrayList<Hit>)
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
    }

    fun queryPixabayApiServiceNext(searchKeyword: String) {
        if (!_isLoading.value!!) {
            _isLoading.postValue(true)
            CustomLogging.errorLog(Demo2ViewModel::class.java, _isLoading)
            _page.value = _page.value!!.toInt() + 1
            queryPixabayApiService(searchKeyword)
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