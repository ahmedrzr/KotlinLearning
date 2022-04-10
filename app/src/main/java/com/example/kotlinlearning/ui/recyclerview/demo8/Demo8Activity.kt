package com.example.kotlinlearning.ui.recyclerview.demo8

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinlearning.adapters.PixabayDemo7Adapter
import com.example.kotlinlearning.adapters.PixabayDemo8Adapter
import com.example.kotlinlearning.databinding.ActivityRecyclerDemoBinding
import com.example.kotlinlearning.models.local.LocalHit
import com.example.kotlinlearning.ui.recyclerview.KotlinLearningViewModelFactory
import com.example.kotlinlearning.ui.recyclerview.repository.PixabayRepository
import com.example.kotlinlearning.utils.*

class Demo8Activity : AppCompatActivity() {
    private var _binding: ActivityRecyclerDemoBinding? = null
    private val binding get() = _binding!!

    private lateinit var demo8ViewModel: Demo8ViewModel
    private var pixabayDemo8Adapter: PixabayDemo8Adapter? = null
    private lateinit var layoutManager: LinearLayoutManager
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
    val localHitList = listOf<LocalHit>(
        LocalHit(1,"https://pixabay.com/get/ga6d649e319f11787cc979addfe935c70715347c5c321fd3c6cf6e7fdfa50f1003e714c02d249fc48fd8f41f805c9f34687af6bb017b8d1c489da310275c1b6ac_1280.jpg"),
        LocalHit(2,	"https://pixabay.com/get/ga0bada99709cab8dc7984d3580a68b55a44add890c78dce7064b5b2fc5f69b189941ebeb4f21258f759882c1734f51f77ee57249942345b36d2ecda99c0cc465_1280.jpg")
        ,
        LocalHit(3,"https://pixabay.com/get/g64c7df259ef76bae0f09e93aa96e4469bb3698a1b586f55895c1a09a3271b24f48583924a47c1269b4cff0ea76cc638b04747b955bc3b033099b5c5f21a9fb54_1280.jpg"),
        LocalHit(4,"https://pixabay.com/get/g6e932f7f6b71490d020eeba0dee7ce1bfa2123e6d01c11d85a9b57b295343bedfa77fe029cbd2b3031752f83d094a41876c73e9465c7c8551b7c836fcbb49fff_1280.jpg")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecyclerDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRecyclerDemo)
        supportActionBar?.let {
            it.title = "RecyclerView Demo 8"
            it.setDisplayHomeAsUpEnabled(true)
        }
        val pixabayRepository = PixabayRepository()
        demo8ViewModel = ViewModelProvider(
            this,
            KotlinLearningViewModelFactory(pixabayRepository)
        )[Demo8ViewModel::class.java]
        initRecyclerView()
        binding.lyContents.let {
            it.includeLyLoading.lyLoading.visibility = View.GONE
            it.includeLyEmpty.lyEmpty.visibility = View.GONE
            it.includeLyError.lyError.visibility = View.GONE
            it.recyclerView.visibility = View.GONE
            it.recyclerView2.visibility = View.VISIBLE
            it.lyCounter.visibility = View.GONE
            it.lySearch.visibility = View.GONE
        }

    }



    private fun initRecyclerView() {
        val size =Point()
        windowManager.defaultDisplay.getSize(size)
        val screenWidth = size.x
        val screenHeight = size.y
        pixabayDemo8Adapter = PixabayDemo8Adapter(localHitList)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
      val customGridLayout = CustomGridManager(resources,screenWidth)
        val gridLayoutManager = GridLayoutManager(this,3)
        binding.lyContents.recyclerView2.let {
            it.layoutManager = customGridLayout
            it.adapter = pixabayDemo8Adapter

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