package com.example.kotlinlearning.ui.recyclerview.demo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinlearning.adapters.PixabayDemo1Adapter
import com.example.kotlinlearning.databinding.ActivityRecyclerDemoBinding
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Status
import com.example.kotlinlearning.ui.recyclerview.KotlinLearningViewModelFactory
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.CustomLogging


class Demo1Activity : AppCompatActivity() {
    private val TAG = Demo1Activity::class.java.simpleName
    private var _binding: ActivityRecyclerDemoBinding? = null
    private val binding get() = _binding!!
    private lateinit var demo1ViewModel: Demo1ViewModel
    private var pixabayDemo1Adapter: PixabayDemo1Adapter? = null
    private lateinit var layoutManager: LinearLayoutManager


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
        initRecyclerView()
        observer()
        initView()
        demo1ViewModel.queryPixabayApiService()


    }

    private fun initRecyclerView() {
        pixabayDemo1Adapter = PixabayDemo1Adapter()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.lyContents.recyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = pixabayDemo1Adapter
        }
    }

    private fun initView() {
        binding.let {
            it.lyContents.btnSearch.setOnClickListener { v: View ->
                demo1ViewModel.setSearchQueryName(it.lyContents.textInputSearch.text.toString())
                demo1ViewModel.queryPixabayApiService()
            }
        }
    }

    private fun observer() {
        demo1ViewModel.pixabayQueryImages.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    CustomLogging.normalLog(Demo1Activity::class.java, "LOADING")
                }
                Status.SUCCESS -> {
                    CustomLogging.normalLog(Demo1Activity::class.java, "SUCCESS")
                    CustomLogging.normalLog(Demo1Activity::class.java, it.data!!.size)
                    pixabayDemo1Adapter?.updateItems(it.data as ArrayList<Hit>)
                }
                else -> {
                    CustomLogging.errorLog(Demo1Activity::class.java, it.message!!)
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