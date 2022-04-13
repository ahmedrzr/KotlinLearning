package com.example.kotlinlearning.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinlearning.databinding.FragmentDemoFragmentBinding
import com.example.kotlinlearning.ui.fragments.demo1.Demo1FragmentActivity
import com.example.kotlinlearning.utils.CustomLogging

class FragmentsDemoFragment : Fragment() {

    private var _binding: FragmentDemoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDemoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.let {
            it.demo1Btn.setOnClickListener {
                CustomLogging.normalLog(FragmentsDemoFragment::class.java, "BTN DEMO1 CLICKED")
                Intent(context,Demo1FragmentActivity::class.java).apply {
                    startActivity(this)
                }
            }
            it . demo2Btn . setOnClickListener {
                CustomLogging.normalLog(FragmentsDemoFragment::class.java, "BTN DEMO2 CLICKED")
            }
            it . demo3Btn . setOnClickListener {
                CustomLogging.normalLog(FragmentsDemoFragment::class.java, "BTN DEMO3 CLICKED")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}