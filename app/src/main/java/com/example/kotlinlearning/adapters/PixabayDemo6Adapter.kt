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
import com.example.kotlinlearning.models.remote.pixabay.Hit
import de.hdodenhof.circleimageview.CircleImageView

class PixabayDemo6Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class PixabayHitHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage = itemView.findViewById<CircleImageView>(R.id.profile_image)
        val previewImage = itemView.findViewById<ImageView>(R.id.preview_image)
        val user = itemView.findViewById<TextView>(R.id.user)
        val userId = itemView.findViewById<TextView>(R.id.user_id)
        val tag = itemView.findViewById<TextView>(R.id.image_tag)

        fun bind(hit: Hit) {
            Glide.with(itemView.context).load(hit.userImageURL)
                .centerCrop()
                .into(profileImage)
            Glide.with(itemView.context).load(hit.largeImageURL)
                .centerCrop()
                .into(previewImage)
            user.text = hit.user
            userId.text = hit.user_id.toString()
            tag.text = hit.tags
            previewImage.setOnClickListener { v ->
                onItemClickLister?.let {
                    it(hit)
                }
            }
        }
    }

    inner class PixabayFilterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage = itemView.findViewById<CircleImageView>(R.id.profile_image)
        val previewImage = itemView.findViewById<ImageView>(R.id.preview_image)
        val user = itemView.findViewById<TextView>(R.id.user)
        val userId = itemView.findViewById<TextView>(R.id.user_id)
        val tag = itemView.findViewById<TextView>(R.id.image_tag)
        val bg = itemView.findViewById<LinearLayout>(R.id.bg_layer)

        fun bindSex(hit: Hit) {
            Glide.with(itemView.context).load(hit.userImageURL)
                .centerCrop()
                .into(profileImage)
            Glide.with(itemView.context).load(hit.largeImageURL)
                .centerCrop()
                .into(previewImage)
            user.text = hit.user
            userId.text = hit.user_id.toString()
            tag.text = hit.tags
            bg.setBackgroundColor(itemView.resources.getColor(R.color.pink_A400))
            previewImage.setOnClickListener { v ->
                onItemClickLister?.let {
                    it(hit)
                }
            }
        }

        fun bindNaked(hit: Hit) {
            Glide.with(itemView.context).load(hit.userImageURL)
                .centerCrop()
                .into(profileImage)
            Glide.with(itemView.context).load(hit.largeImageURL)
                .centerCrop()
                .into(previewImage)
            user.text = hit.user
            userId.text = hit.user_id.toString()
            tag.text = hit.tags
            bg.setBackgroundColor(itemView.resources.getColor(R.color.gray_200))
            previewImage.setOnClickListener { v ->
                onItemClickLister?.let {
                    it(hit)
                }
            }
        }

        fun bindNude(hit: Hit) {
            Glide.with(itemView.context).load(hit.userImageURL)
                .centerCrop()
                .into(profileImage)
            Glide.with(itemView.context).load(hit.largeImageURL)
                .centerCrop()
                .into(previewImage)
            user.text = hit.user
            userId.text = hit.user_id.toString()
            tag.text = hit.tags
            bg.setBackgroundColor(itemView.resources.getColor(R.color.red_A700))
            previewImage.setOnClickListener { v ->
                onItemClickLister?.let {
                    it(hit)
                }
            }
        }
    }

    inner class PixabayLoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hit: Hit) {}
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean =
            oldItem.largeImageURL.equals(newItem.largeImageURL)
    }
    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING_VIEW -> PixabayLoadingHolder(
                parent.inflate(
                    R.layout.bottom_loading_itemt_row,
                    false
                )
            )
            SEX_VIEW -> PixabayFilterHolder(
                parent.inflate(
                    R.layout.pixabay_item_filter_row,
                    false
                )
            )
            NAKED_VIEW -> PixabayFilterHolder(
                parent.inflate(
                    R.layout.pixabay_item_filter_row,
                    false
                )
            )
            NUDE_VIEW -> PixabayFilterHolder(
                parent.inflate(
                    R.layout.pixabay_item_filter_row,
                    false
                )
            )
            else -> PixabayHitHolder(parent.inflate(R.layout.pixabay_item_row, false))
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hit = differ.currentList[position]
        when (holder.itemViewType) {
            LOADING_VIEW -> (holder as PixabayLoadingHolder).bind(hit)
            SEX_VIEW -> (holder as PixabayFilterHolder).bindSex(hit)
            NAKED_VIEW -> (holder as PixabayFilterHolder).bindNaked(hit)
            NUDE_VIEW -> (holder as PixabayFilterHolder).bindNude(hit)
            else -> (holder as PixabayHitHolder).bind(hit)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            differ.currentList[position].tags?.contains("LOADING") == true -> LOADING_VIEW
            differ.currentList[position].tags?.contains("sex") == true -> SEX_VIEW
            differ.currentList[position].tags?.contains("naked") == true -> NAKED_VIEW
            differ.currentList[position].tags?.contains("nude") == true -> NUDE_VIEW
            else -> HIT_VIEW
        }
    }

    private var onItemClickLister: ((Hit) -> Unit)? = null
    fun setOItemClickListener(listener: ((Hit) -> Unit)) {
        onItemClickLister = listener
    }


    companion object {
        const val HIT_VIEW = 0
        const val LOADING_VIEW = 1
        const val SEX_VIEW = 2
        const val NAKED_VIEW = 3
        const val NUDE_VIEW = 4
    }
}