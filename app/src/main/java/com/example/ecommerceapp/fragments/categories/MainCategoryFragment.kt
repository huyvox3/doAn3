package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.FeaturedProductAdapter
import com.example.ecommerceapp.adapters.OnSaleProductAdapter
import com.example.ecommerceapp.adapters.SpecialProductAdapter
import com.example.ecommerceapp.databinding.FragmentMainCategoryBinding
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.utils.showBottomNav
import com.example.ecommerceapp.viewmodels.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainCategoryFragment:Fragment() {

    private lateinit var binding: FragmentMainCategoryBinding

    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var featuredProductAdapter: FeaturedProductAdapter
    private lateinit var onSaleProductAdapter: OnSaleProductAdapter


    private val viewModel by viewModels<MainCategoryViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRv()
        setupOnSaleProductRv()
        setupFeaturedProductRv()
        featuredProdsLc()
        specialProdsLc()
        onSaleProdsLc()






        binding.nestScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v,_,scrollY,_,_ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY){
                viewModel.fetchFeaturedProducts()
            }
        })
    }

    private fun setupFeaturedProductRv() {
        featuredProductAdapter = FeaturedProductAdapter()
        binding.featuredProductsRv.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = featuredProductAdapter
        }
        featuredProductAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }

    private fun setupOnSaleProductRv() {
        onSaleProductAdapter = OnSaleProductAdapter()
        binding.onSaleRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = onSaleProductAdapter
        }

        onSaleProductAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }


    private fun setupSpecialProductRv() {

        specialProductAdapter = SpecialProductAdapter()
        binding.SpecialProductRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductAdapter
        }

        specialProductAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

    }


    private fun showLoading() {
        binding.mainCatProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.mainCatProgressBar.visibility = View.GONE
    }

    private fun featuredProdsLc(){
        lifecycleScope.launch {
            viewModel.featuredProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.featuredProductProgessBar.visibility = View.VISIBLE
                    }

                    is Resource.Success ->{
                        featuredProductAdapter.differ.submitList(it.data)
                        binding.featuredProductProgessBar.visibility = View.GONE
                    }

                    is Resource.Error ->{
                        binding.featuredProductProgessBar.visibility = View.GONE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }
    private fun specialProdsLc(){
        lifecycleScope.launch {
            viewModel.specialProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showLoading()
                    }

                    is Resource.Success ->{
                        specialProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error ->{
                        hideLoading()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }
    private fun onSaleProdsLc(){
        lifecycleScope.launch {
            viewModel.onSaleProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showLoading()
                    }

                    is Resource.Success ->{
                        onSaleProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error ->{
                        hideLoading()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        showBottomNav()
    }
}