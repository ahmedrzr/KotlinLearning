package com.example.kotlinlearning.ui.fragments.demo1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinlearning.R
import com.example.kotlinlearning.adapters.PixabayDemo6Adapter
import com.example.kotlinlearning.databinding.FragmentPixabayListBinding
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Resource
import com.example.kotlinlearning.network.apihelpers.Status
import com.example.kotlinlearning.ui.recyclerview.KotlinLearningViewModelFactory
import com.example.kotlinlearning.ui.recyclerview.PixabayImageActivity
import com.example.kotlinlearning.ui.recyclerview.demo2.Demo2Activity
import com.example.kotlinlearning.ui.recyclerview.demo6.Demo6ViewModel
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.Constants
import com.example.kotlinlearning.utils.CustomLogging
import com.example.kotlinlearning.utils.LayoutViewType
import com.example.kotlinlearning.utils.PaginationScrollListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PixabayListFragment : Fragment() {

    private var _binding: FragmentPixabayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var demo6ViewModel: Demo6ViewModel
    private var pixabayDemo6Adapter: PixabayDemo6Adapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading = false
    private var searchKeyword = Constants.API_DEFAULT_SEARCH_QUERY1
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPixabayListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pixabayRepository = PixabayRepository()
        demo6ViewModel = ViewModelProvider(
            this,
            KotlinLearningViewModelFactory(pixabayRepository)
        )[Demo6ViewModel::class.java]
        initRecyclerView()
        observer()
        initView()
        demo6ViewModel.queryPixabayApiService(searchKeyword)
    }
    private fun initView() {

        binding.let {
            it.textInputSearch.setText(demo6ViewModel.queryName)
            it.btnSearch.setOnClickListener { v ->
                if (it.textInputSearch.text.toString().isNotEmpty()) {
                    searchKeyword = it.textInputSearch.text.toString()
                    demo6ViewModel.queryPixabayApiService(searchKeyword)
                }
            }
        }
    }

    private fun initRecyclerView() {
        pixabayDemo6Adapter = PixabayDemo6Adapter()
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = pixabayDemo6Adapter
            it.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    demo6ViewModel.queryPixabayApiService(searchKeyword)
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
        pixabayDemo6Adapter!!.setOItemClickListener {

        }
    }


    private fun observer() {
        demo6ViewModel.pixabayQueryImages.observe(viewLifecycleOwner, Observer {
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
        demo6ViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            it.let {
                isLoading = it
            }
        })
        demo6ViewModel.isLastPage.observe(viewLifecycleOwner, Observer { isLastPage = it })
    }

    private fun updateLayout(status: LayoutViewType, hitData: Resource<List<Hit>>? = null) {
        when (status) {
            LayoutViewType.LOADING -> {
                if (hitData != null) {
                    binding.let {

                        it.includeLyEmpty.lyEmpty.visibility = View.GONE
                        it.includeLyError.lyError.visibility = View.GONE
                        it.recyclerView.visibility = View.VISIBLE
                        it.lyCounter.visibility = View.GONE
                        if (pixabayDemo6Adapter!!.differ.currentList.size == 0) {
                            it.includeLyLoading.lyLoading.visibility = View.VISIBLE
                        } else {
                            hitData.data?.let { it1 ->
                                var data = demo6ViewModel.imagesList.value
                                pixabayDemo6Adapter!!.differ.submitList(data)
                            }
                        }
                    }
                }
            }
            LayoutViewType.EMPTY -> {
                binding.let {
                    it.includeLyLoading.lyLoading.visibility = View.GONE
                    it.includeLyEmpty.lyEmpty.visibility = View.VISIBLE
                    it.includeLyError.lyError.visibility = View.GONE
                    it.recyclerView.visibility = View.GONE
                    it.lyCounter.visibility = View.GONE
                }
            }
            LayoutViewType.ERROR -> {
                binding.let {
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
                binding.let {
                    it.includeLyLoading.lyLoading.visibility = View.GONE
                    it.includeLyEmpty.lyEmpty.visibility = View.GONE
                    it.includeLyError.lyError.visibility = View.GONE
                    it.recyclerView.visibility = View.VISIBLE
                    it.lyCounter.visibility = View.VISIBLE
                }
                if (hitData != null) {
                    if (hitData.data != null) {
                        val data = demo6ViewModel.imagesList.value
                        pixabayDemo6Adapter?.differ?.submitList(data)
                        binding.currentCounter.text = hitData.data.size.toString()
                        binding.totalCounter.text = demo6ViewModel.totalHits
                    }
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}