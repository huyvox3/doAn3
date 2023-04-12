package com.example.ecommerceapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.adapters.FeaturedProductAdapter
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Array.get
import javax.inject.Inject
@HiltViewModel
class MainCategoryViewModel@Inject constructor(
    private val firestore : FirebaseFirestore
):ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts:StateFlow<Resource<List<Product>>> = _specialProducts


    private val _featuredProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val featuredProducts:StateFlow<Resource<List<Product>>> = _featuredProducts


    private val _onSaleProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val onSaleProducts:StateFlow<Resource<List<Product>>> = _onSaleProducts

    private val pagingInfo = PagingInfo()

    init{
        fetchSpecialProducts()
        fetchFeaturedProducts()
        fetchOnSaleProducts()
    }
    fun fetchSpecialProducts(){
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
         firestore.collection("Products")
             .whereEqualTo("category","Special Product")
             .get()
             .addOnSuccessListener {
                 val specialProductList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductList))
                }
             }.addOnFailureListener{
                 viewModelScope.launch {
                     _specialProducts.emit(Resource.Error(it.message.toString()))
                 }
             }
    }


    fun fetchOnSaleProducts(){
        viewModelScope.launch {
            _onSaleProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereNotEqualTo("offerPercentage",null)
            .orderBy("offerPercentage", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener {
                val onSaleProductList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _onSaleProducts.emit(Resource.Success(onSaleProductList))
                }

            }.addOnFailureListener{
                viewModelScope.launch {
                    _onSaleProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchFeaturedProducts(){

        if (!pagingInfo.isPagingEnd){


            viewModelScope.launch {
                _featuredProducts.emit(Resource.Loading())
            }
            firestore.collection("Products")
                .whereEqualTo("category","Featured Product")
                .limit(pagingInfo.featuredProductPages * 10)
                .get()
                .addOnSuccessListener {

                    val featuredProductList = it.toObjects(Product::class.java)

                    pagingInfo.isPagingEnd = featuredProductList == pagingInfo.oldFeaturedProducts

                    pagingInfo.oldFeaturedProducts = featuredProductList

                    viewModelScope.launch {
                        _featuredProducts.emit(Resource.Success(featuredProductList))
                    }

                    pagingInfo.featuredProductPages++
                }.addOnFailureListener{
                    viewModelScope.launch {
                        _featuredProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }


    internal data class PagingInfo(
         var featuredProductPages:Long = 1,
        var oldFeaturedProducts: List<Product> = emptyList(),
        var isPagingEnd:Boolean = false,
    ){

    }
}