package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.ViewpagerImageItemLayoutBinding

class ViewPager2Images :RecyclerView.Adapter<ViewPager2Images.ViewPager2ImagesViewHolder>(){
    inner class ViewPager2ImagesViewHolder(val binding:ViewpagerImageItemLayoutBinding):ViewHolder(binding.root) {
        fun bind(imgPath:String) {
            Glide.with(itemView).load(imgPath).into(binding.imgProductDetails)
        }
    }

    private val diffCallback = object: DiffUtil.ItemCallback<String>(){
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImagesViewHolder {
        return ViewPager2ImagesViewHolder(
            ViewpagerImageItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPager2ImagesViewHolder, position: Int) {
        val img = differ.currentList[position]
        holder.bind(img)
    }


    override fun getItemCount(): Int = differ.currentList.size
}