package com.example.ecommerceapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.CartItem
import com.example.ecommerceapp.firebase.FiresbaseCommon
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firesbaseCommon: FiresbaseCommon
) :ViewModel(){

    private val _addToCartItem = MutableStateFlow<Resource<CartItem>>(Resource.Unspecified())
    val addToCart  = _addToCartItem.asStateFlow()

    init {


    }

    fun addUpdateProd(cartItem: CartItem){

        viewModelScope.launch {

            _addToCartItem.emit(Resource.Loading())

        }
        firestore.collection("users").document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id",cartItem.product.id)
            .get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){
                        addNewProd(cartItem)
                    }else{
                        val product = it.first().toObject(cartItem::class.java)
                        if (product == cartItem){
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartItem)
                        }else{
                            addNewProd(cartItem)

                        }
                    }

                }
            }.addOnFailureListener {
                viewModelScope.launch {

                    _addToCartItem.emit(Resource.Error(it.message.toString()))

                }

            }
    }

    private fun addNewProd(cartItem: CartItem){
        firesbaseCommon.addProdsToCart(cartItem){addedItem,e ->
            if (e == null){
                viewModelScope.launch {
                    _addToCartItem.emit(Resource.Success(addedItem!!))
                }
            }
            else{
                viewModelScope.launch {
                    _addToCartItem.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    private fun increaseQuantity(documentId:String, cartItem: CartItem){
        firesbaseCommon.increaseQuantity(documentId){ _, e->
            viewModelScope.launch {
                if (e == null){
                    viewModelScope.launch {
                        _addToCartItem.emit(Resource.Success(cartItem!!))
                    }
                }else{
                    _addToCartItem.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }
}