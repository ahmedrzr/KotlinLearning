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

class PixabayDemo7Adapter(val thingsList:List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ThingsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag = itemView.findViewById<TextView>(R.id.text)

        fun bind(thing: String) {
            tag.text = thing

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThingsHolder(parent.inflate(R.layout.item, false))

    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val thing = thingsList[position]
        (holder as ThingsHolder).bind(thing)

    }

    override fun getItemCount(): Int = thingsList.size




}