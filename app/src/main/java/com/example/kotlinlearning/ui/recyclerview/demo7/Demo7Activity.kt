package com.example.kotlinlearning.ui.recyclerview.demo7

import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinlearning.adapters.PixabayDemo6Adapter
import com.example.kotlinlearning.adapters.PixabayDemo7Adapter
import com.example.kotlinlearning.databinding.ActivityRecyclerDemoBinding
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.network.apihelpers.Resource
import com.example.kotlinlearning.network.apihelpers.Status
import com.example.kotlinlearning.ui.recyclerview.KotlinLearningViewModelFactory
import com.example.kotlinlearning.ui.recyclerview.PixabayImageActivity
import com.example.kotlinlearning.ui.recyclerview.demo2.Demo2Activity
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.*

class Demo7Activity : AppCompatActivity() {
    private var _binding: ActivityRecyclerDemoBinding? = null
    private val binding get() = _binding!!

    private lateinit var demo7ViewModel: Demo7ViewModel
    private var pixabayDemo7Adapter: PixabayDemo7Adapter? = null
    private lateinit var layoutManager: CustomLayoutManager
    private var isLoading = false
    private var searchKeyword = Constants.API_DEFAULT_SEARCH_QUERY1
    private var isLastPage = false

    private val thingsList = listOf(
        "banana",
        "apple",
        "pear",
        "strawberry",
        "cherry",
        "plum",
        "orange",
        "kiwi",
        "kumquat",
        "wolfberry",
        "dragonfruit"
    )

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
        demo7ViewModel = ViewModelProvider(
            this,
            KotlinLearningViewModelFactory(pixabayRepository)
        )[Demo7ViewModel::class.java]
        initRecyclerView()
        binding.lyContents.let {
            it.includeLyLoading.lyLoading.visibility = View.GONE
            it.includeLyEmpty.lyEmpty.visibility = View.GONE
            it.includeLyError.lyError.visibility = View.GONE
            it.recyclerView.visibility = View.VISIBLE
            it.lyCounter.visibility = View.GONE
            it.lySearch.visibility = View.GONE
        }

    }


    private fun initRecyclerView() {

        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        val screenWidth = size.x
        val screenHeight = size.y
        CustomLogging.normalLog(Demo7Activity::class.java, "SCREEN WIDTH : $screenWidth")
        CustomLogging.errorLog(Demo7Activity::class.java, "SCREEN HEIGHT _ $screenHeight")

        pixabayDemo7Adapter = PixabayDemo7Adapter(thingsList)
      // layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager = CustomLayoutManager(resources,screenWidth)
        binding.lyContents.recyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = pixabayDemo7Adapter
         //   it.layoutParams.height = screenWidth/2
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