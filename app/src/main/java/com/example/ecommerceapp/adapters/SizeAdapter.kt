package com.example.ecommerceapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.databinding.SizeRvItemBinding

class SizeAdapter:RecyclerView.Adapter<SizeAdapter.SizeViewHolder>()  {

    private var selectedPosition = -1

    inner class SizeViewHolder(private val binding: SizeRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String,position: Int) {
            binding.sizeTv.text = size
            if (position == selectedPosition){
                binding.apply {
                    imgShadow.visibility = View.VISIBLE

                }
            }
            else{
                binding.apply {
                    imgShadow.visibility = View.INVISIBLE

                }

            }
        }
    }


    private val diffCalback = object: DiffUtil.ItemCallback<String>(){
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCalback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        return SizeViewHolder(
            SizeRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size,position)
        holder.itemView.setOnClickListener{
            if (selectedPosition >= 0){
                notifyItemChanged((selectedPosition))
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }


    override fun getItemCount(): Int = differ.currentList.size

    var onItemClick:((String)-> Unit)? = null
}