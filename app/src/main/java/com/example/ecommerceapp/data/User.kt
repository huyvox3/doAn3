package com.example.ecommerceapp.data

data class User(
    var firstName:String,
    var lastname:String,
    var email: String,
    var imagePath: String ="",

    ){
    constructor():this("","","","")
}
