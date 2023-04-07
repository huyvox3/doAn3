package com.example.ecommerceapp.fragments.loginResgister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAccountOptionsBinding

class AccountOptionsFragment:Fragment() {
    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginAccountOptionsBtn.setOnClickListener{
            findNavController().navigate(R.id.loginFragment)
        }
        binding.registerAccountOptionsBtn.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)
        }
    }
}