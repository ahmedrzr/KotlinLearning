package com.example.kotlinlearning.ui.recyclerview.demo8

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinlearning.adapters.PixabayDemo5Adapter
import com.example.kotlinlearning.adapters.PixabayDemo6Adapter
import com.example.kotlinlearning.adapters.PixabayDemo8Adapter
import com.example.kotlinlearning.databinding.ActivityRecyclerDemoBinding
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Resource
import com.example.kotlinlearning.network.apihelpers.Status
import com.example.kotlinlearning.ui.recyclerview.KotlinLearningViewModelFactory
import com.example.kotlinlearning.ui.recyclerview.PixabayImageActivity
import com.example.kotlinlearning.ui.recyclerview.demo2.Demo2Activity
import com.example.kotlinlearning.ui.recyclerview.demo5.Demo5ViewModel
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.Constants
import com.example.kotlinlearning.utils.CustomLogging
import com.example.kotlinlearning.utils.LayoutViewType
import com.example.kotlinlearning.utils.PaginationScrollListener

class Demo8Activity : AppCompatActivity() {
    private var _binding: ActivityRecyclerDemoBinding? = null
    private val binding get() = _binding!!

    private lateinit var demo8ViewModel: Demo8ViewModel
    private var pixabayDemo8Adapter: PixabayDemo8Adapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading = false
    private var searchKeyword = Constants.API_DEFAULT_SEARCH_QUERY1
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecyclerDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRecyclerDemo)
        supportActionBar?.let {
            it.title = "RecyclerView Demo 4"
            it.setDisplayHomeAsUpEnabled(true)
        }
        val pixabayRepository = PixabayRepository()
        demo8ViewModel = ViewModelProvider(
            this,
            KotlinLearningViewModelFactory(pixabayRepository)
        )[Demo8ViewModel::class.java]
        initRecyclerView()
        observer()
        initView()
        demo8ViewModel.queryPixabayApiService(searchKeyword)

    }

    private fun initView() {

        binding.let {
            it.lyContents.textInputSearch.setText(demo8ViewModel.queryName)
            it.lyContents.btnSearch.setOnClickListener { v ->
                if (it.lyContents.textInputSearch.text.toString().isNotEmpty()) {
                    searchKeyword = it.lyContents.textInputSearch.text.toString()
                    demo8ViewModel.queryPixabayApiService(searchKeyword)
                }
            }
        }
    }

    private fun initRecyclerView() {
        pixabayDemo8Adapter = PixabayDemo8Adapter()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.lyContents.recyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = pixabayDemo8Adapter
            it.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    demo8ViewModel.queryPixabayApiService(searchKeyword)
                }

                override fun isLastPage(): Boolean {
                    CustomLogging.errorLog(Demo2Activity::class.java, "isLastPage = $isLastPage")
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    CustomLogging.errorLog(Demo2Activity::class.java, isLoading)
                    return isLoading

                }
            })
        }
        pixabayDemo8Adapter!!.setOItemClickListener {
            Intent(this, PixabayImageActivity::class.java).apply {
                putExtra(Constants.PIXABAY, it)
                startActivity(this)
            }
        }
    }


    private fun observer() {
        demo8ViewModel.pixabayQueryImages.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    updateLayout(LayoutViewType.LOADING, it)
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
        demo8ViewModel.isLoading.observe(this, Observer {
            it.let {
                isLoading = it
            }
        })
        demo8ViewModel.isLastPage.observe(this, Observer { isLastPage = it })
    }

    private fun updateLayout(status: LayoutViewType, hitData: Resource<List<Hit>>? = null) {
        when (status) {
            LayoutViewType.LOADING -> {
                if (hitData != null) {
                    binding.lyContents.let {

                        it.includeLyEmpty.lyEmpty.visibility = View.GONE
                        it.includeLyError.lyError.visibility = View.GONE
                        it.recyclerView.visibility = View.VISIBLE
                        it.lyCounter.visibility = View.GONE
                        if (pixabayDemo8Adapter!!.differ.currentList.size == 0) {
                            it.includeLyLoading.lyLoading.visibility = View.VISIBLE
                        } else {
                            hitData.data?.let { it1 ->
                                var data = demo8ViewModel.imagesList.value
                                pixabayDemo8Adapter!!.differ.submitList(data)
                            }
                        }
                    }
                }
            }
            LayoutViewType.EMPTY -> {
                binding.lyContents.let {
                    it.includeLyLoading.lyLoading.visibility = View.GONE
                    it.includeLyEmpty.lyEmpty.visibility = View.VISIBLE
                    it.includeLyError.lyError.visibility = View.GONE
                    it.recyclerView.visibility = View.GONE
                    it.lyCounter.visibility = View.GONE
                }
            }
            LayoutViewType.ERROR -> {
                binding.lyContents.let {
                    it.includeLyLoading.lyLoading.visibility = View.GONE
                    it.includeLyEmpty.lyEmpty.visibility = View.GONE
                    it.includeLyError.lyError.visibility = View.VISIBLE
                    it.recyclerView.visibility = View.GONE
                    it.lyCounter.visibility = View.GONE
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
                    it.lyCounter.visibility = View.VISIBLE
                }
                if (hitData != null) {
                    if (hitData.data != null) {
                        val data = demo8ViewModel.imagesList.value
                        pixabayDemo8Adapter?.differ?.submitList(data)
                        binding.lyContents.currentCounter.text = hitData.data.size.toString()
                        binding.lyContents.totalCounter.text = demo8ViewModel.totalHits
                    }
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}