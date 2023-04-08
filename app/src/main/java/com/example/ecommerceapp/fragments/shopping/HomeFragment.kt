package com.example.ecommerceapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.adapters.HomeViewpagerAdapter
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments =  arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CuppboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment(),

        )

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.homeViewPager.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.homeViewPager){tab,position ->

            when(position){
                0 ->tab.text = "Main"
                1 ->tab.text = "Chair"
                2 ->tab.text = "Cuppboard"
                3 ->tab.text = "Table"
                4 ->tab.text = "Accessory"
                5 ->tab.text = "Furniture"


            }
        }.attach()
    }

}