package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.databinding.FragmentCuppboardCategoryBinding

class CuppboardFragment:Fragment() {
    private lateinit var binding:FragmentCuppboardCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCuppboardCategoryBinding.inflate(inflater)
        return binding.root
    }
}