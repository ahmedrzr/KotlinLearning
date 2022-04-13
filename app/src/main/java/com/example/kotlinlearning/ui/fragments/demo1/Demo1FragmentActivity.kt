package com.example.kotlinlearning.ui.fragments.demo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinlearning.databinding.ActivityDemoFragmentBinding


class Demo1FragmentActivity : AppCompatActivity() {
    private var _binding: ActivityDemoFragmentBinding? = null
    private val bindging get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDemoFragmentBinding.inflate(layoutInflater)
        setContentView(bindging.root)
        val toolbar = bindging.toolbar
        setSupportActionBar(toolbar).apply {
            title = "DEMO1 FRAGMENT"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}