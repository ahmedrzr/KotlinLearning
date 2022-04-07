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
import com.example.kotlinlearning.utils.PixabayDiffCallback
import de.hdodenhof.circleimageview.CircleImageView

class PixabayDemo4Adapter : RecyclerView.Adapter<PixabayDemo4Adapter.PixabayHolder>() {
    private var pixabayImgList = ArrayList<Hit>()


    inner class PixabayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profileImage = itemView.findViewById<CircleImageView>(R.id.profile_image)
        val previewImage = itemView.findViewById<ImageView>(R.id.preview_image)
        val user = itemView.findViewById<TextView>(R.id.user)
        val userId = itemView.findViewById<TextView>(R.id.user_id)
        val tag = itemView.findViewById<TextView>(R.id.image_tag)

        fun bind(hit: Hit) {
            previewImage.setImageURI(Uri.parse(hit.userImageURL))

            Glide.with(itemView.context).load(hit.largeImageURL)
                .centerCrop()
                .into(previewImage)
            user.text = hit.user
            userId.text = hit.user_id.toString()
            tag.text = hit.tags
        }

        fun bindEmpty() {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PixabayHolder {
        val inflatedView = parent.inflate(R.layout.pixabay_item_row, false)
        return PixabayHolder(inflatedView)
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun onBindViewHolder(holder: PixabayHolder, position: Int) {
        val hit = differ.currentList[position]
        holder.bind(hit)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Hit, newItem: Hit) =
            oldItem.previewURL == newItem.previewURL

    }
    val differ = AsyncListDiffer(this, diffCallback)
    override fun getItemCount() =  differ.currentList.size


    fun updateItems(data: ArrayList<Hit>) {

    }


}