package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.databinding.FragmentAccessoryCategoryBinding

class AccessoryFragment:Fragment() {
    private lateinit var binding: FragmentAccessoryCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccessoryCategoryBinding.inflate(inflater)
        return binding.root
    }
}