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
    private var viewRight = 0
    private var viewTop = 0
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
        var i = 0
        for (index in 0..1) {
            val view = recycler.getViewForPosition(index)
                addView(view)
            if (viewRight == totalWidth) {
                i = -1
                viewTop=viewHeight
            }
            i++
            layoutChildView(i, viewWidth, view, viewHeight)
        }

    }

    private fun layoutChildView(
        i: Int, viewWidthWithSpacing: Int, view: View,
        viewHighrWithSpacing: Int
    ) {
        val left = i * viewWidthWithSpacing
        viewRight = left + viewWidthWithSpacing
        val top =  viewTop
        val bottom = top + viewHighrWithSpacing


        measureChild(view, viewWidthWithSpacing, viewHighrWithSpacing)


        layoutDecorated(view, left, top, viewRight, bottom)
    }
}