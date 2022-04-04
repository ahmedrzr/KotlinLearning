package com.example.kotlinlearning.ui.recyclerview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinlearning.ui.recyclerview.demo1.Demo1ViewModel
import com.example.kotlinlearning.ui.recyclerview.demo2.Demo2ViewModel
import com.example.kotlinlearning.ui.recyclerview.demo3.Demo3ViewModel
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import java.lang.IllegalArgumentException

class KotlinLearningViewModelFactory(private val pixabayRepository: PixabayRepository? = null) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Demo1ViewModel::class.java))
            return Demo1ViewModel(pixabayRepository!!) as T
        else
            if (modelClass.isAssignableFrom(Demo2ViewModel::class.java))
                return Demo2ViewModel(pixabayRepository!!) as T
            else
                if (modelClass.isAssignableFrom(Demo3ViewModel::class.java))
                    return Demo3ViewModel(pixabayRepository!!) as T
                else
                    throw IllegalArgumentException("ViewModel Not Found!!")
    }
}