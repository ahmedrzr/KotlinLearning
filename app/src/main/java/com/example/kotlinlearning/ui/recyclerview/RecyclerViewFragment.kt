package com.example.kotlinlearning.ui.recyclerview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinlearning.databinding.FragmentRecyclerviewBinding
import com.example.kotlinlearning.ui.recyclerview.demo1.Demo1Activity
import com.example.kotlinlearning.ui.recyclerview.demo2.Demo2Activity
import com.example.kotlinlearning.ui.recyclerview.demo3.Demo3Activity
import com.example.kotlinlearning.ui.recyclerview.demo4.Demo4Activity
import com.example.kotlinlearning.ui.recyclerview.demo5.Demo5Activity
import com.example.kotlinlearning.ui.recyclerview.demo6.Demo6Activity
import com.example.kotlinlearning.ui.recyclerview.demo7.Demo7Activity
import com.example.kotlinlearning.ui.recyclerview.demo8.Demo8Activity
import com.example.kotlinlearning.utils.Constants

private val TAG = RecyclerViewFragment::class.java.simpleName

class RecyclerViewFragment : Fragment() {

    private var _binding: FragmentRecyclerviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.let {
            it.demo1Btn.setOnClickListener {
                Intent(context, Demo1Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo1)
                    startActivity(this)
                }
            }
            it.demo2Btn.setOnClickListener {
                Intent(context, Demo2Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo2)
                    startActivity(this)
                }
            }
            it.demo3Btn.setOnClickListener {
                Intent(context, Demo3Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo3)
                    startActivity(this)
                }
            }
            it.demo4Btn.setOnClickListener {
                Intent(context, Demo4Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo4)
                    startActivity(this)
                }
            }
            it.demo5Btn.setOnClickListener {
                Intent(context, Demo5Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo5)
                    startActivity(this)
                }
            }
            it.demo6Btn.setOnClickListener {
                Intent(context, Demo6Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo6)
                    startActivity(this)
                }
            }
            it . demo7Btn . setOnClickListener {
                Intent(context, Demo7Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo6)
                    startActivity(this)
                }
            }
            it . demo8Btn . setOnClickListener {
                Intent(context, Demo8Activity::class.java).apply {
                    putExtra(Constants.demo, Constants.demo6)
                    startActivity(this)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}