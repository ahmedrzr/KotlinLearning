package com.example.kotlinlearning.utils

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinlearning.R

class TestLayoutManager(resources: Resources, private val screenWidth: Int): RecyclerView.LayoutManager() {

    private val TAG = "CustomLayoutManager"
    private var horizontalScrollOffset = 0

    private val viewWidth = resources.getDimensionPixelSize(R.dimen.item_width)
    private val recyclerViewHeight = (resources.getDimensionPixelSize(R.dimen.recyclerview_height)).toDouble()
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams? {
        return RecyclerView.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT
        )
    }
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        fill(recycler, state)
    }

    private fun fill(recycler: RecyclerView.Recycler, state:  RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        val firstVisiblePosition = Math.floor(horizontalScrollOffset.toDouble() / viewWidth.toDouble()).toInt()
        val lastVisiblePosition = (horizontalScrollOffset + screenWidth) / viewWidth
         CustomLogging.normalLog(TestLayoutManager::class.java, "viewWidth : ${viewWidth.toDouble()}" +
                 " screenWidth : ${screenWidth} \\n horizontalScrollOffset: $horizontalScrollOffset")
        CustomLogging.normalLog(TestLayoutManager::class.java, "firstVisiblePosition : $firstVisiblePosition" +
                "  \n lastVisiblePosition: $lastVisiblePosition")
        for (index in firstVisiblePosition..lastVisiblePosition) {
            var recyclerIndex = index % itemCount
            CustomLogging.normalLog(TestLayoutManager::class.java, "recyclerIndex : $recyclerIndex" )
            if (recyclerIndex < 0) {
                recyclerIndex += itemCount
            }
            val view = recycler.getViewForPosition(recyclerIndex)
            addView(view)

            layoutChildView(index, viewWidth, view)
        }
//        val scrapListCopy = recycler.scrapList.toList()
//        scrapListCopy.forEach {
//            recycler.recycleView(it.itemView)
//        }
    }

    private fun layoutChildView(i: Int, viewWidthWithSpacing: Int, view: View) {
        val left = i * viewWidthWithSpacing - horizontalScrollOffset
        val right = left + viewWidth
        val top = getTopOffsetForView(left + viewWidth/2)
        val bottom = top + viewWidth

        measureChild(view, viewWidth, viewWidth)

        layoutDecorated(view, left, top, right, bottom)
    }

    private fun getTopOffsetForView(viewCentreX: Int): Int {
        CustomLogging.normalLog(TestLayoutManager::class.java, "viewCentreX : ***** ${viewCentreX}*****" )
        val s: Double = screenWidth.toDouble() / 2
        val h: Double = recyclerViewHeight - viewWidth.toDouble()
        CustomLogging.errorLog(TestLayoutManager::class.java, "s : ${s}" +
                "h: $h")
       // val radius: Double = ( h*h + s*s ) / (h*2)
        val radius: Double = (screenWidth.toDouble() ) / (2)
        CustomLogging.normalLog(TestLayoutManager::class.java, "radius  ${radius}" )
        val cosAlpha = (s - viewCentreX) / radius
        CustomLogging.errorLog(TestLayoutManager::class.java, "viewCentreX : ${viewCentreX}" +
                "cosAlpha: $cosAlpha")
        val alpha = Math.acos(MathUtils.clamp(cosAlpha, -1.0, 1.0))
        CustomLogging.normalLog(TestLayoutManager::class.java, "alpha : ${alpha}" )
        val yComponent = radius - (radius * Math.sin(alpha))
        CustomLogging.normalLog(TestLayoutManager::class.java, "yComponent : ${yComponent}" )
        return yComponent.toInt()
    }

    override fun canScrollHorizontally(): Boolean = true

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state:  RecyclerView.State): Int {
        horizontalScrollOffset += dx
        fill(recycler, state)
//        CustomLogging.normalLog(TestLayoutManager::class.java, "dx : $dx  & horizontalScrollOffset: $horizontalScrollOffset")
       return dx
    }
}