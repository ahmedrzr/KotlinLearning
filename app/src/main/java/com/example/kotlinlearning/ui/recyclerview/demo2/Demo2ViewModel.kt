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
    fun setSearchQuery(query: String) = _queryName.postValue(query)

    private var lastQueryString = MutableLiveData<String>()

    private var _totalHits = MutableLiveData<String>()
    val totalHits get() = _totalHits.value!!

    var _page = MutableLiveData(1)
    val page get() = _page.value!!

    private var _isLastPage = MutableLiveData(false)
    var isLastPage = _isLastPage.value!!

    private var _isLoading = MutableLiveData(false)
    val isLoading = _isLoading

    var isPageExist = MutableLiveData(true)
    fun queryPixabayApiService(query: String = queryName) {
        _isLoading.postValue(true)
        if(_isLastPage.value!!) {
            _isLoading.postValue(false)
            return
        }
        if (lastQueryString.value != null) {
            if (lastQueryString.value.toString() != queryName) {
                _page.value = 1
            }
        }
        pixabayQueryImagesResponse.postValue(Resource.loading(null))
        job = CoroutineScope(Dispatchers.IO + exceptioHandler).launch {
            val response =
                pixabayRepository.queryPixabayImagesByPagination(
                    queryName,
                    page.toString()
                )
            response.let {
                if (it.isSuccessful) {
                    CustomLogging.normalLog(Demo2ViewModel::class.java, "SUCCESS")
                    withContext(Dispatchers.Main) {
                        _totalHits.value = it.body()!!.totalHits.toString()
                        CustomLogging.normalLog(Demo2ViewModel::class.java, "PAGE  ${page}")
                        lastQueryString.value = queryName
                        if (page > 1) {
                            try {
                                var list = imagesList.value!!
                                list.addAll(it.body()!!.hits as ArrayList<Hit>)
                                imagesList.value = list

                                CustomLogging.normalLog(Demo2ViewModel::class.java, "SIZE  ${ imagesList.value!!.size}")
                            }
                            catch (e:Exception){
                                CustomLogging.errorLog(Demo2ViewModel::class.java, "error  ${e.message}")
                            }
                        } else {
                            imagesList.value = (it.body()!!.hits as ArrayList<Hit>)
                        }
                        _isLastPage.value = imagesList.value!!.size == totalHits.toInt()
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

    fun queryPixabayApiServiceNext() {
        if (!_isLoading.value!!) {
            _isLoading.postValue(true)
            CustomLogging.errorLog(Demo2ViewModel::class.java, _isLoading)
            _page.value = _page.value!!.toInt() + 1
            queryPixabayApiService()
        } else {
            CustomLogging.normalLog(Demo2ViewModel::class.java, _isLoading)
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