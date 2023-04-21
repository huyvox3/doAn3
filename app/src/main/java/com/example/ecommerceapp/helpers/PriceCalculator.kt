package com.example.ecommerceapp.helpers

fun Float?.getItemPrice(price: Float):Float{
    if (this == null ){
        return price
    }

    val remainingPricePercentage = 1f - this
    val newPrice = remainingPricePercentage  * price

    return newPrice
}