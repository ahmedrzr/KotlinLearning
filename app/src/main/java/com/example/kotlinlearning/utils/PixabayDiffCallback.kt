package com.example.kotlinlearning.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.kotlinlearning.models.remote.pixabay.Hit

class PixabayDiffCallback constructor(
    private val oldListHits: ArrayList<Hit>,
    private val newListHits: ArrayList<Hit>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldListHits.size

    override fun getNewListSize() = newListHits.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldListHits[oldItemPosition].id == newListHits[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldListHits[oldItemPosition].previewURL == newListHits[newItemPosition].previewURL
    }
}