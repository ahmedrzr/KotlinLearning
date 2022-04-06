package com.example.kotlinlearning.ui.recyclerview.demo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kotlinlearning.R
import com.example.kotlinlearning.adapters.PixabayDemo1Adapter
import com.example.kotlinlearning.databinding.ActivityRecyclerDemoBinding
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Resource
import com.example.kotlinlearning.network.apihelpers.Status
import com.example.kotlinlearning.ui.recyclerview.KotlinLearningViewModelFactory
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.CustomLogging
import com.example.kotlinlearning.utils.LayoutViewType


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
            it.lyContents.textInputSearch.setText(demo1ViewModel.getSearchQueryName)
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
                    updateLayout(LayoutViewType.LOADING, null)
                }
                Status.SUCCESS -> {
                    if (it.data?.size!! > 0)
                        updateLayout(LayoutViewType.DATA, it)
                    else
                        updateLayout(LayoutViewType.EMPTY, null)
                }
                else -> {
                    updateLayout(LayoutViewType.ERROR, null)
                }
            }
        })
    }

    private fun updateLayout(status: LayoutViewType, hitData: Resource<List<Hit>>? = null) {
        when (status) {
            LayoutViewType.LOADING -> {
                binding.lyContents.let {
                    it.includeLyLoading.lyLoading.visibility = View.VISIBLE
                    it.includeLyEmpty.lyEmpty.visibility = View.GONE
                    it.includeLyError.lyError.visibility = View.GONE
                    it.recyclerView.visibility = View.GONE
                    Glide.with(this).load(R.drawable.loading_gif)
                        .into(it.includeLyLoading.loadingImg)
                }
            }
            LayoutViewType.EMPTY -> {
                binding.lyContents.let {
                    it.includeLyLoading.lyLoading.visibility = View.GONE
                    it.includeLyEmpty.lyEmpty.visibility = View.VISIBLE
                    it.includeLyError.lyError.visibility = View.GONE
                    it.recyclerView.visibility = View.GONE
                    binding.hitCount.text = "0"
                }
            }
            LayoutViewType.ERROR -> {
                binding.lyContents.let {
                    it.includeLyLoading.lyLoading.visibility = View.GONE
                    it.includeLyEmpty.lyEmpty.visibility = View.GONE
                    it.includeLyError.lyError.visibility = View.VISIBLE
                    it.recyclerView.visibility = View.GONE
                    if (hitData != null) {
                        it.includeLyError.errorMsg.text = hitData.message
                    }
                }
            }
            LayoutViewType.DATA -> {
                binding.lyContents.let {
                    it.includeLyLoading.lyLoading.visibility = View.GONE
                    it.includeLyEmpty.lyEmpty.visibility = View.GONE
                    it.includeLyError.lyError.visibility = View.GONE
                    it.recyclerView.visibility = View.VISIBLE
                }
                if (hitData != null) {
                    pixabayDemo1Adapter?.updateItems(hitData.data as ArrayList<Hit>)
                    binding.hitCount.text = hitData.data!!.size.toString()
                }
            }
        }
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