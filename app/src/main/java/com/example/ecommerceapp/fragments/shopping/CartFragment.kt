package com.example.ecommerceapp.fragments.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.os.Bundle
import com.example.ecommerceapp.databinding.FragmentCartBinding

class CartFragment:Fragment() {
    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }
}