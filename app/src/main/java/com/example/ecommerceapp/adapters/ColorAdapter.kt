package com.example.ecommerceapp.adapters

import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommerceapp.databinding.ColorRvItemBinding

class ColorAdapter:RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedPosition = -1

    inner class ColorViewHolder(private val binding:ColorRvItemBinding):ViewHolder(binding.root) {
        fun bind(color: Int,position: Int) {
            val imgDrawable = ColorDrawable(color)
            binding.imgColor.setImageDrawable(imgDrawable)
            if (position == selectedPosition){
                binding.apply {
                    imgShadow.visibility = View.VISIBLE
                    pickedIv.visibility = View.VISIBLE
                }
            }
            else{
                binding.apply {
                    imgShadow.visibility = View.INVISIBLE
                    pickedIv.visibility = View.INVISIBLE
                }

            }
        }
    }


    private val diffCalback = object: DiffUtil.ItemCallback<Int>(){
        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCalback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            ColorRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color,position)
        holder.itemView.setOnClickListener{
            if (selectedPosition >= 0){
                notifyItemChanged((selectedPosition))
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }


    override fun getItemCount(): Int = differ.currentList.size

    var onItemClick:((Int)-> Unit)? = null
}