package com.example.kotlinlearning.ui.recyclerview.demo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinlearning.R
import com.example.kotlinlearning.databinding.ActivityRecyclerDemoBinding
import com.example.kotlinlearning.network.apihelpers.Status
import com.example.kotlinlearning.ui.recyclerview.KotlinLearningViewModelFactory
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository


class Demo1Activity : AppCompatActivity() {
    private val TAG = Demo1Activity::class.java.simpleName
    private var _binding: ActivityRecyclerDemoBinding? = null
    private val binding get() = _binding!!
    private lateinit var demo1ViewModel: Demo1ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecyclerDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRecyclerDemo)
        supportActionBar?.let {
            it.title = "RecyclerView Demo 1"
            it.setDisplayHomeAsUpEnabled(true)
        }
        val pixabayRepository = PixabayRepository()
        demo1ViewModel = ViewModelProvider(
            this,
            KotlinLearningViewModelFactory(pixabayRepository)
        )[Demo1ViewModel::class.java]
        demo1ViewModel.queryPixabayApiService("sex")
        observer()

    }

    private fun observer() {
        demo1ViewModel.pixabayQueryImages.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    Log.i(TAG, "observer: LOADING")
                }
                Status.SUCCESS -> {
                    Log.i(TAG, "observer: SUCCESS")
                }
                else -> {
                    Log.e(TAG, "observer: ${it.message}")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}