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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}