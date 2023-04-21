package com.example.ecommerceapp.firebase

import com.example.ecommerceapp.data.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FiresbaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

) {
    private val cartCollection = firestore.collection("users").document(auth.uid!!).collection("cart")

    fun addProdsToCart(cartItem: CartItem, onResult: (CartItem?, Exception?)-> Unit){
        cartCollection.document().set(cartItem)
            .addOnSuccessListener {
                onResult(cartItem,null)
            }.addOnFailureListener {
                onResult(null,it)
            }

    }

    fun increaseQuantity(documentId:String, onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction { transition->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val proddToObject = document.toObject(CartItem::class.java)

            proddToObject?.let { cartItem ->
                val newQuantity = cartItem.quantity +1
                val newProdObject = cartItem.copy(quantity =  newQuantity)
                transition.set(documentRef,newProdObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }

    fun decreaseQuantity(documentId:String, onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction { transition->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val proddToObject = document.toObject(CartItem::class.java)

            proddToObject?.let { cartItem ->
                val newQuantity = cartItem.quantity -1
                val newProdObject = cartItem.copy(quantity =  newQuantity)
                transition.set(documentRef,newProdObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
    enum class QuantityChanging{
        INCREASE,DECREASE
    }
}