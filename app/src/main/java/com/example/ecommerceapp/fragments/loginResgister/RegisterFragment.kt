package com.example.ecommerceapp.fragments.loginResgister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.databinding.FragmentRegisterBinding
import com.example.ecommerceapp.utils.RegisterValidation
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment:Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        return binding.root
    }

    fun clear(){
       binding.apply {
           firstNameEt.text.clear()
           lastNameEt.text.clear()
           emailRegisterEt.text.clear()
           passwordRegisterEt.text.clear()
       }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.haveAccountTv.setOnClickListener{
            findNavController().navigate(R.id.loginFragment)
        }

        binding.apply {
            registerRegisterBtn.setOnClickListener{

               val user = User(
                   firstNameEt.text.toString(),
                   lastNameEt.text.toString(),
                   emailRegisterEt.text.toString()
               )
                val password = passwordRegisterEt.text.toString()
                viewModel.createNativeAccount(user,password)



            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.registerRegisterBtn.startAnimation()
                    }
                    is Resource.Success ->{
                        Log.e("test",it.message.toString())
                        Log.e("ok","account created")
                        Toast.makeText(context, "Signed Up", Toast.LENGTH_SHORT).show()
                        clear()

                        findNavController().navigate(R.id.loginFragment)

                        binding.registerRegisterBtn.revertAnimation()
                    }
                    is Resource.Error ->{
                        Log.e("test error",it.message.toString())
                        if (it.message.toString().contains("email")){
                            withContext(Dispatchers.Main){
                                binding.emailRegisterEt.apply {
                                    requestFocus()
                                    error = it.message
                                }
                            }
                        }
//                        if(it.message.toString().contains("password")){
//                            withContext(Dispatchers.Main){
//                                binding.passwordRegisterEt.apply {
//                                    requestFocus()
//                                    error = it.message
//                                }
//                            }
//                        }


                        binding.registerRegisterBtn.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{
                if (it.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.emailRegisterEt.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
                if (it.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.passwordRegisterEt.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }
}