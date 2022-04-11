package com.example.kotlinlearning.utils

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinlearning.R

class CustomGridManager(resources: Resources, private val screenWidth: Int) :
    RecyclerView.LayoutManager() {

    // private val viewWidth = resources.getDimensionPixelSize(R.dimen.item_width)

    private var totalWidth: Int = 0
    private var totalHeight = 0
    private var viewWidth = 0
    private var viewHeight = 0
    private var viewTopReset = 0
    private var viewLeftReset = 0
    private val recyclerViewHeight =
        (resources.getDimensionPixelSize(R.dimen.recyclerview_height)).toDouble()

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        totalWidth = width - paddingRight - paddingLeft
        totalHeight = height - paddingBottom - paddingTop
        viewWidth = totalWidth / 2
        viewHeight = totalHeight / 2
        fill(recycler, state)
    }

    private fun fill(recycler: RecyclerView.Recycler, state: RecyclerView.State) {

        for (itemPosition in 0 until itemCount) {
            val view = recycler.getViewForPosition(itemPosition)
            addView(view)
            layoutChildView(itemPosition, viewWidth, view, viewHeight)
        }

    }

    private fun layoutChildView(
        i: Int, viewWidthWithSpacing: Int, view: View,
        viewHighrWithSpacing: Int
    ) {
        if (i <= 1) {
            val left = i * viewWidthWithSpacing
            val right = left + viewWidthWithSpacing
            val top = 0
            val bottom = top + viewHighrWithSpacing
            measureChild(view, viewWidthWithSpacing, viewHighrWithSpacing)
            layoutDecorated(view, left, top, right, bottom)
        } else {
            val left = viewLeftReset * viewWidthWithSpacing
            val right = left + viewWidthWithSpacing
            val top = viewHighrWithSpacing
            val bottom = top + viewHighrWithSpacing
            measureChild(view, viewWidthWithSpacing, viewHighrWithSpacing)
            layoutDecorated(view, left, top, right, bottom)
            viewLeftReset++
            CustomLogging.normalLog(CustomGridManager::class.java, "i : $i")
        }


    }
}