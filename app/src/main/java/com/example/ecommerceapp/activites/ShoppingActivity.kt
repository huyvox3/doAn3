package com.example.ecommerceapp.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ActivityShoppingBinding
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.viewmodels.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartViewModel> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingNavHostFragment)
        binding.bottomNav.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewModel.cartItems.collectLatest {
                when (it){
                    is Resource.Success ->{
                        val count = it.data?.size ?:0

                        Log.e("Bottom nav count", "onCreate: ${count}", )


                        binding.bottomNav.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_black)
                        }

                    }
                    else -> Unit
                }
            }
        }
    }
}