package com.example.ecommerceapp.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.databinding.FeaturedProductRvItemBinding
import com.example.ecommerceapp.databinding.OnSaleRvItemBinding
import com.example.ecommerceapp.helpers.getItemPrice

class FeaturedProductAdapter: RecyclerView.Adapter<FeaturedProductAdapter.FeaturedProductViewHolder>() {

    inner class FeaturedProductViewHolder(private val binding: FeaturedProductRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {

                Glide.with(itemView).load(product.images[0]).into(featuredProductImgView)
                featuredProductNameTv.text = product.name
                featuredProductPriceTv.text = "$ ${product.price.toString()}"
                product.offerPercentage?.let {

                    val newPrice = (1f - (it / 100)) * product.price

                    featuredProducNewpriceTv.text ="$ ${String.format("%.2f",newPrice)}"
                    featuredProductPriceTv.paintFlags = featuredProductPriceTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                }

                if (product.offerPercentage == null){
                    featuredProducNewpriceTv.visibility = View.INVISIBLE
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedProductAdapter.FeaturedProductViewHolder {
        return FeaturedProductViewHolder(
            FeaturedProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FeaturedProductAdapter.FeaturedProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    override fun getItemCount() = differ.currentList.size

    var onClick:((Product) -> Unit)? = null
}