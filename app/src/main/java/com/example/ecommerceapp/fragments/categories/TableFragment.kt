package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding
import com.example.ecommerceapp.databinding.FragmentTableCategoryBinding

class TableFragment:BaseCategoryFragment() {
    private lateinit var binding: FragmentTableCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTableCategoryBinding.inflate(inflater)
        return binding.root
    }
}