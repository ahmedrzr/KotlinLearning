package com.example.kotlinlearning.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinlearning.R
import com.example.kotlinlearning.models.remote.pixabay.Hit
import com.example.kotlinlearning.utils.LayoutViewType
import com.example.kotlinlearning.utils.PixabayDiffCallback
import de.hdodenhof.circleimageview.CircleImageView

class PixabayDemo5Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var pixabayImgList = ArrayList<Hit>()
    private var isLoading = false

    inner class PixabayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        }
    }

    inner class PixabayHolderFilter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage = itemView.findViewById<CircleImageView>(R.id.profile_image)
        val previewImage = itemView.findViewById<ImageView>(R.id.preview_image)
        val user = itemView.findViewById<TextView>(R.id.user)
        val userId = itemView.findViewById<TextView>(R.id.user_id)
        val tag = itemView.findViewById<TextView>(R.id.image_tag)
        fun bindFilter(hit: Hit) {
            previewImage.setImageURI(Uri.parse(hit.userImageURL))
            Glide.with(itemView.context).load(hit.userImageURL)
                .centerCrop()
                .into(profileImage)
            Glide.with(itemView.context).load(hit.largeImageURL)
                .centerCrop()
                .into(previewImage)
            user.text = hit.user
            userId.text = hit.user_id.toString()
            tag.text = hit.tags
        }
    }

    inner class PixabayHolderLoading(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindLoading(hit: Hit) {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            0 ->
                PixabayHolder(parent.inflate(R.layout.pixabay_item_row, false))
            1 -> PixabayHolderFilter(parent.inflate(R.layout.pixabay_item_filter_row, false))
            else -> PixabayHolderLoading(parent.inflate(R.layout.bottom_loading_itemt_row, false))
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hit = differ.currentList[position]
        when (holder.itemViewType) {
            0 -> (holder as PixabayHolder).bind(hit)
            1 -> (holder as PixabayHolderFilter).bindFilter(hit)
            2 -> (holder as PixabayHolderLoading).bindLoading(hit)

        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Hit, newItem: Hit) =
            oldItem.previewURL == newItem.previewURL

        override fun getChangePayload(oldItem: Hit, newItem: Hit): Any? {
            return super.getChangePayload(oldItem, newItem)
        }

    }
    val differ = AsyncListDiffer(this, diffCallback)
    override fun getItemCount() = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            differ.currentList[position].tags?.contains("sex") == true -> 1
            differ.currentList[position].tags?.contains("LOADING") == true -> 2
            else -> 0
        }
    }

    fun addLoadingView(triggerLoading: Boolean) {
        isLoading = triggerLoading
    }


}
