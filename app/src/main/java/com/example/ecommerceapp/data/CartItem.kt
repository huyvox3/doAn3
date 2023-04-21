package com.example.ecommerceapp.data

data class CartItem(
    val product: Product,
    val quantity: Int,
    val selectedColor: Int? = null,
    val selectedSize: String? = null,


){
    constructor():this(Product(),1,null,null)
}
