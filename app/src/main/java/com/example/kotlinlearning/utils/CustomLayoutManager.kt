package com.example.kotlinlearning.utils

import android.content.res.Resources
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.kotlinlearning.R

class CustomLayoutManager(resources: Resources, private val screenWidth: Int) :
    RecyclerView.LayoutManager() {

    private var horizontalScrollOffset: Int = 0
    private val viewWidth = resources.getDimensionPixelSize(R.dimen.item_width)
    override fun generateDefaultLayoutParams(): LayoutParams =
        LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

    override fun canScrollHorizontally(): Boolean = true

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: Recycler?,
        state: RecyclerView.State?
    ): Int {
        horizontalScrollOffset = +dx
        fill(recycler!!)
        return dx
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        fill(recycler)

    }

    private fun fill(recycler: Recycler) {
        detachAndScrapAttachedViews(recycler)
        for (i in 0 until itemCount) {
            val left = i * viewWidth-horizontalScrollOffset
            val right = left + viewWidth
            val top = 0
            val bottom = top + viewWidth

            val view = recycler.getViewForPosition(i)
            addView(view)
            measureChild(view, viewWidth, viewWidth)
            layoutDecorated(view, left, top, right, bottom)
        }
        val scrapyListCopy = recycler.scrapList.toList()
        scrapyListCopy.forEach {
            recycler.recycleView(it.itemView)
        }
    }

}