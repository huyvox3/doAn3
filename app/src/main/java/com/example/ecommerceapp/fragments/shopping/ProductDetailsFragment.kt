package com.example.ecommerceapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.activites.ShoppingActivity
import com.example.ecommerceapp.adapters.ColorAdapter
import com.example.ecommerceapp.adapters.SizeAdapter
import com.example.ecommerceapp.adapters.ViewPager2Images
import com.example.ecommerceapp.data.CartItem
import com.example.ecommerceapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.utils.hideBottomNav
import com.example.ecommerceapp.viewmodels.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment:Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizeAdapter by lazy { SizeAdapter() }
    private val colorAdapter by lazy { ColorAdapter() }
    private var selectedColor:Int? =null
    private var selectedSize:String? = null
    private val viewModel by viewModels<DetailsViewModel> ()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNav()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product


        setupSizeRv()
        setupColorRv()
        setupViewpager()

        binding.apply {
            addToCartBtn.setOnClickListener{
                viewModel.addUpdateProd(CartItem(product,1,selectedColor,selectedSize))

            }
            productNameTv.text = product.name
            productDesTV.text = product.description
            productPriceTv.text = "$ ${product.price}"

            closeBtn.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.addToCartBtn.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.addToCartBtn.revertAnimation()
                        binding.addToCartBtn.setBackgroundColor(resources.getColor(R.color.g_black))
                        Toast.makeText(requireContext(),"Added",Toast.LENGTH_SHORT)
                    }

                    is Resource.Error ->{
                        binding.addToCartBtn.stopAnimation()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        viewPagerAdapter.differ.submitList(product.images)
        product.sizes?.let {
            binding.sizeLinearLayout.visibility = View.VISIBLE
            sizeAdapter.differ.submitList(product.sizes)
        }
       product.colors?.let {
           binding.colorLinearLayout.visibility = View.VISIBLE
           colorAdapter.differ.submitList(product.colors)
       }

    }

    private fun setupViewpager() {
        binding.apply{
            productImgVp.adapter = viewPagerAdapter
        }
    }

    private fun setupColorRv() {
        colorAdapter.onItemClick = {
            selectedColor = it
        }
        binding.productColorRv.apply {
            adapter = colorAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }

    }

    private fun setupSizeRv() {
        sizeAdapter.onItemClick ={
            selectedSize = it
        }

        binding.productSizeRv.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        }
    }
}