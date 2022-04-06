package com.example.kotlinlearning.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var layoutManager = linearLayoutManager
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemsCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLastPage() && !isLoading()) {
            if ((visibleItemsCount + firstVisibleItemPosition) >= totalItemCount &&
                firstVisibleItemPosition >= 0
            )
                loadMoreItems()
        }

    }

    protected abstract fun loadMoreItems()
    protected abstract fun isLastPage(): Boolean
    protected abstract fun isLoading(): Boolean
}