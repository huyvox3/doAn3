package com.example.ecommerceapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    val addressTitle:String,
    val fullName :String,
    val street:String,
    val phone:String,
    val city:String,

) :Parcelable{
    constructor():this("","","","","")

}
