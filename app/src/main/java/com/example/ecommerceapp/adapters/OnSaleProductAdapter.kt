package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.databinding.OnSaleRvItemBinding


class OnSaleProductAdapter:RecyclerView.Adapter<OnSaleProductAdapter.OnSaleProductViewHolder>() {
    inner class OnSaleProductViewHolder(private val binding: OnSaleRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
           binding.apply {
               Glide.with(itemView).load(product.images[0]).into(onSaleImgView)
               onSaleNameTv.text = product.name
                oldPriceTv.text= "$ ${product.price.toString()}"
             product.offerPercentage?.let {
                 val newPrice = (1f - it) * product.price
                 newPriceTv.text ="$ ${String.format("%.2f",newPrice)}"
             }
           }
        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnSaleProductAdapter.OnSaleProductViewHolder {
        return OnSaleProductViewHolder(
            OnSaleRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: OnSaleProductAdapter.OnSaleProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount() = differ.currentList.size
}