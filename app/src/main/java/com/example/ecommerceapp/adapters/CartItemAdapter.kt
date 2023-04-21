package com.example.ecommerceapp.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.data.CartItem
import com.example.ecommerceapp.databinding.CartItemBinding
import com.example.ecommerceapp.helpers.getItemPrice

class CartItemAdapter:RecyclerView.Adapter<CartItemAdapter.CartItemsViewHolder> (){

    inner class CartItemsViewHolder(val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cartItems: CartItem){
            binding.apply {
                Glide.with(itemView).load(cartItems.product.images[0]).into(itemImgIv)
                itemNameTv.text = cartItems.product.name
                itemQuantityTv.text = cartItems.quantity.toString()
                val newPrice = (1f - (cartItems.product.offerPercentage!! / 100)) * cartItems.product.price
                itemPriceTv.text = "$ ${String.format("%.2f",newPrice)}"

                colorCircleIv.setImageDrawable(ColorDrawable(cartItems.selectedColor?: Color.TRANSPARENT))
                tvCartProductSize.text = cartItems.selectedSize?:"".also { sizeCircleIv.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }

            }
        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<CartItem>(){
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        return CartItemsViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val items = differ.currentList[position]
        holder.bind(items)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(items)

        }

        holder.binding.minusBtn.setOnClickListener{
            onMinusClick?.invoke(items)
        }
        holder.binding.plusBtn.setOnClickListener{
            onPlusClick?.invoke(items)
        }
    }

    override fun getItemCount() = differ.currentList.size


    var onItemClick:((CartItem) -> Unit)? = null
    var onPlusClick:((CartItem) -> Unit)? = null
    var onMinusClick:((CartItem) -> Unit)? = null
}