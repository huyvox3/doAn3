package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.adapters.FeaturedProductAdapter
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment:Fragment() {
    private lateinit var binding: FragmentBaseCategoryBinding
    private lateinit var offAdapter: FeaturedProductAdapter
    private lateinit var featuredProductAdapter: FeaturedProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
        setupOfferRv()
        setupFeaturedRv()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setupFeaturedRv() {
        featuredProductAdapter = FeaturedProductAdapter()
        binding.featuredProductsRv.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = featuredProductAdapter
        }
    }

    private fun setupOfferRv() {
        offAdapter = FeaturedProductAdapter()
        binding.offRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offAdapter
        }
    }
}