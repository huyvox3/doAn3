package com.example.ecommerceapp.viewmodels

import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ecommerceapp.data.CartItem
import com.example.ecommerceapp.firebase.FiresbaseCommon
import com.example.ecommerceapp.helpers.getItemPrice
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject  constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firesbaseCommon: FiresbaseCommon
):ViewModel(){

    private val _cartItems = MutableStateFlow<Resource<List<CartItem>>>(Resource.Unspecified())

    val cartItems = _cartItems.asStateFlow()
    private val _deleteDialog = MutableSharedFlow<CartItem>()
    val delelteDialog = _deleteDialog.asSharedFlow()

    private var cartItemDocuments = emptyList<DocumentSnapshot>()
    val productsPrice = cartItems.map {
        when(it){
            is Resource.Success ->{
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    fun deteleItem(cartItem: CartItem){
        val index = cartItems.value.data?.indexOf(cartItem)
        if (index != null && index != -1){
            val documentId = cartItemDocuments[index].id
            firestore.collection("users")
                .document(auth.uid!!)
                .collection("cart")
                .document(documentId)
                .delete()
        }
    }


    private fun calculatePrice(data: List<CartItem>): Any {
        return data.sumByDouble { item ->

           ( (1f - (item.product.offerPercentage!! / 100f))  * item.product.price * item.quantity).toDouble()
        }
    }

    init {
        getCartItems()
    }

    private fun getCartItems(){
        viewModelScope.launch {
            _cartItems.emit(Resource.Loading())
        }

        firestore.collection("users")
            .document(auth.uid!!)
            .collection("cart")
            .addSnapshotListener{value,error ->
                if (error != null || value == null){
                    viewModelScope.launch { _cartItems.emit(Resource.Error(error?.message.toString())) }
                }

                else{
                    cartItemDocuments = value.documents
                    val cartItems = value.toObjects(CartItem::class.java)
                    viewModelScope.launch { _cartItems.emit(Resource.Success(cartItems)) }

                }
            }
    }

    fun changeQuantity(
        carItems:CartItem,
        quantityChanging: FiresbaseCommon.QuantityChanging,
    ){


        val index = cartItems.value.data?.indexOf(carItems)


        if (index != null && index != -1){
            val documentId = cartItemDocuments[index].id
            when(quantityChanging){
                FiresbaseCommon.QuantityChanging.INCREASE ->{
                    viewModelScope.launch { _cartItems.emit(Resource.Loading()) }
                    increaseQuantity(documentId)
                }
                FiresbaseCommon.QuantityChanging.DECREASE ->{
                    if (carItems.quantity == 1){

                        viewModelScope.launch {
                            _deleteDialog.emit(carItems)
                        }
                        return
                    }
                    viewModelScope.launch { _cartItems.emit(Resource.Loading()) }
                    decreaseQuantity(documentId)
                }
                else -> Unit
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firesbaseCommon.decreaseQuantity(documentId){result,e ->
            if (e != null){
                viewModelScope.launch { _cartItems.emit(Resource.Error(e.message.toString())) }
            }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firesbaseCommon.increaseQuantity(documentId){result, e ->
            if (e != null){
                viewModelScope.launch {_cartItems.emit(Resource.Error(e.message.toString())) }
            }
        }
    }
}