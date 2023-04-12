package com.example.ecommerceapp.fragments.categories


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.FeaturedProductAdapter
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding


//@AndroidEntryPoint
open class BaseCategoryFragment:Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding


    protected val offAdapter: FeaturedProductAdapter by lazy { FeaturedProductAdapter() }
    protected val featuredProductAdapter: FeaturedProductAdapter by lazy { FeaturedProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(layoutInflater)
        if (binding != FragmentBaseCategoryBinding.inflate(layoutInflater)){
            Log.e("binding",  binding.root.toString(), )



        }

        if (this::binding.isInitialized){
            Log.e("BINDING",  "Initialized", )

                setupOfferRv()
                setupFeaturedRv()

            binding.nestScrollViewBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _ ->
                if (v.getChildAt(0).bottom <= v.height + scrollY){
                    onFeaturedPagingRequest()
                }
            })
        }



        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)



    }
    open fun onOfferPagingRequest(){

    }

    open fun onFeaturedPagingRequest(){

    }
    private fun setupFeaturedRv() {
        binding.featuredProductRv.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = featuredProductAdapter
            Log.e("RV", "Featured")
        }
    }

    private fun setupOfferRv() {
        binding.offRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offAdapter
            Log.e("RV", "Off")
        }

        binding.offRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })
    }
}