package com.example.ecommerceapp.fragments.loginResgister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.activites.ShoppingActivity
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.dialog.setupBottomSheetDialog
import com.example.ecommerceapp.utils.Resource
import com.example.ecommerceapp.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment:Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private  val viewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {

            binding.forgotPasswordLoginTv.setOnClickListener{
                setupBottomSheetDialog { email->

                    viewModel.resetPassword(email)



                }
            }



            dontTv.setOnClickListener{
                findNavController().navigate(R.id.registerFragment)
            }
            loginLoginBtn.setOnClickListener{
                val email = emailLoginEt.text.toString()
                val password = passwordLoginEt.text.toString()
                viewModel.login(email,password)

            }
        }


        lifecycleScope.launch{
            viewModel.resetPassword.collect{
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        Snackbar.make(requireView(),"Reset link was sent to your email.",Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.loginLoginBtn.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.loginLoginBtn.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also{
                            it ->
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(it)

                        }
                    }
                    is Resource.Error ->{
                        binding.loginLoginBtn.revertAnimation()
//                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                       if (it.message.toString().contains("password")){
                           withContext(Dispatchers.Main){
                               binding.passwordLoginEt.apply {
                                   requestFocus()
                                   getText().clear()
                                   error = it.message
                               }
                           }
                       }
                        if (it.message.toString().contains("email")){
                            withContext(Dispatchers.Main){
                                binding.emailLoginEt.apply {
                                    requestFocus()
                                    error = it.message
                                }
                            }
                        }

                    }
                    else -> Unit
                }
            }
        }
    }

}