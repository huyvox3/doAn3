package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding
import com.example.ecommerceapp.databinding.FragmentTableCategoryBinding
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.viewmodels.CategoryViewModel
import com.example.ecommerceapp.viewmodels.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
@AndroidEntryPoint
class TableFragment:BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel>{
        BaseCategoryViewModelFactory(firestore, Category.Table)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offProdsLifecyle()
        featuredProdsLifeCylce()
    }

    private fun offProdsLifecyle(){
        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showOfferLoading()
                    }
                    is Resource.Success ->{
                        offAdapter.differ.submitList(it.data)
                        hideOfferLoading()

                    }
                    is Resource.Error ->{
                        hideOfferLoading()
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else ->Unit
                }
            }
        }
    }


    private fun featuredProdsLifeCylce(){
        lifecycleScope.launchWhenStarted {
            viewModel.featureProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showFeaturedProductsLoading()

                    }
                    is Resource.Success ->{
                        featuredProductAdapter.differ.submitList(it.data)
                        hideFeaturedProductsLoading()

                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideFeaturedProductsLoading()
                    }
                    else ->Unit
                }
            }
        }
    }
}