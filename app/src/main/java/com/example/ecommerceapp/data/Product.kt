package com.example.ecommerceapp.data

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val quantity:String,
    val images: List<String>
)
{
    constructor():this("0","","",0,0f,null,null,null,"",images = emptyList())
}
