package com.example.ecommerceapp.fragments.shopping

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.adapters.CartItemAdapter
import com.example.ecommerceapp.databinding.FragmentCartBinding
import com.example.ecommerceapp.firebase.FiresbaseCommon
import com.example.ecommerceapp.utils.VerticalItemDecoration
import com.example.ecommerceapp.viewmodels.CartViewModel
import kotlinx.coroutines.flow.collectLatest

class CartFragment:Fragment() {
    private lateinit var binding: FragmentCartBinding
    private  val cartAdapter by lazy {CartItemAdapter()}
    private  val viewModels by activityViewModels<CartViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRv()

        lifecycleScope.launchWhenStarted {
            viewModels.productsPrice.collectLatest {
                it?.let {
                    binding.totalPriceTv.text = "$ $it"
                }
            }
        }
        cartAdapter.onItemClick={
            val b= Bundle().apply { putParcelable("product",it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
        }

        cartAdapter.onPlusClick={
            viewModels.changeQuantity(it,FiresbaseCommon.QuantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick={
            viewModels.changeQuantity(it,FiresbaseCommon.QuantityChanging.DECREASE)
        }

        lifecycleScope.launchWhenStarted {
            viewModels.delelteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete this item form your cart?")
                    setNegativeButton("Cancel"){dialog,_ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes"){diaglog,_ ->
                        viewModels.deteleItem(it)
                        diaglog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }



        lifecycleScope.launchWhenStarted {
            viewModels.cartItems.collectLatest {
                when(it){
                     is Resource.Loading ->{
                        binding.cartPg.visibility = View.VISIBLE
                     }

                    is Resource.Success ->{
                        binding.cartPg.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherViews()
                        }else{
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }

                    }
                    is Resource.Error ->{
                            binding.cartPg.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }


                    else -> Unit
                }
            }
        }
    }

    private fun showOtherViews() {
        binding.apply {
            cartItemRv.visibility = View.VISIBLE
            totalConstraintLayout.visibility = View.VISIBLE
            checkoutBtn.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {

        binding.apply {
            cartItemRv.visibility = View.GONE
            totalConstraintLayout.visibility = View.GONE
            checkoutBtn.visibility = View.GONE
        }

    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE

        }

    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }

    }

    private fun setupCartRv() {
        binding.cartItemRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = cartAdapter

            addItemDecoration(VerticalItemDecoration())

        }
    }
}