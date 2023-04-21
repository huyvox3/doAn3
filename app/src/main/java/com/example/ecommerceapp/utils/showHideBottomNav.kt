package com.example.ecommerceapp.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.R
import com.example.ecommerceapp.activites.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNav(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNav
    )
    bottomNavigationView.visibility = View.GONE
}

fun Fragment.showBottomNav(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNav
    )
    bottomNavigationView.visibility = View.VISIBLE
}