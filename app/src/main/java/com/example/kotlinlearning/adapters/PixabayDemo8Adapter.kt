package com.example.kotlinlearning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinlearning.R
import com.example.kotlinlearning.models.local.LocalHit
import com.example.kotlinlearning.models.remote.pixabay.Hit
import de.hdodenhof.circleimageview.CircleImageView

class PixabayDemo8Adapter(val localHits: List<LocalHit>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class PixabayHitHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val previewImage: ImageView = itemView.findViewById<ImageView>(R.id.preview_image)

        fun bind(hit: LocalHit) {
            Glide.with(itemView.context).load(hit.largeImageURL)
                .placeholder(itemView.context.getDrawable(R.drawable.world_bg))
                .centerCrop()
                .into(previewImage)

            // previewImage.setImageDrawable(itemView.context.getDrawable(R.drawable.world_bg))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PixabayHitHolder(parent.inflate(R.layout.pixabay_item_img_row, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hit = localHits[position]
        (holder as PixabayHitHolder).bind(hit)
    }

    override fun getItemCount() = localHits.size


    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)


}