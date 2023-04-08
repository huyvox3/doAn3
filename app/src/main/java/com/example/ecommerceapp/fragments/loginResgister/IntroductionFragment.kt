package com.example.ecommerceapp.fragments.loginResgister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.activites.ShoppingActivity
import com.example.ecommerceapp.databinding.FragmentIntroductionBinding

import com.example.ecommerceapp.viewmodels.IntrocductionViewModel
import com.example.ecommerceapp.viewmodels.IntrocductionViewModel.Companion.SHOPPING_ACTIVITY
import com.example.ecommerceapp.viewmodels.IntrocductionViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment:Fragment() {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntrocductionViewModel> ()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect{
                when(it){
                    SHOPPING_ACTIVITY ->{
                        Intent(requireActivity(),ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }


                    ACCOUNT_OPTION_FRAGMENT ->{
                      findNavController().navigate(it)

                    }
                    else -> Unit
                }
            }
        }



        binding.startBtn.setOnClickListener{
            viewModel.startButtonClick()
            findNavController().navigate(R.id.accountOptionsFragment)
        }
    }
}