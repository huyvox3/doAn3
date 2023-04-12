package com.example.ecommerceapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) :ViewModel(){
    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())

    val offerProducts = _offerProducts.asStateFlow()

    private val _featuredProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())

    val featureProducts = _featuredProducts.asStateFlow()


    init {
        fetchOfferProducts()
        fetchFeaturedProducts()
    }
    fun fetchOfferProducts(){
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereNotEqualTo("offerPercentage",null)
            .get()
            .addOnSuccessListener {

                val products = it.toObjects(Product::class.java)
                Log.e("VIEWMODEL", "fetchOfferProducts: OK", )
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }


    fun fetchFeaturedProducts(){

        viewModelScope.launch {
            _featuredProducts.emit(Resource.Loading())
        }

        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                Log.e("VIEWMODEL", "fetchFeaturedProducts: OK", )
                viewModelScope.launch {
                    _featuredProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _featuredProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}