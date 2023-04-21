package com.example.ecommerceapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.UserInfo
import com.example.ecommerceapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserinfoViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel(){
    private val _addNewInfo = MutableStateFlow<Resource<UserInfo>>(Resource.Unspecified())
    val addNewInfo = _addNewInfo.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addAddress(userInfo: UserInfo){

      if (validateInput(userInfo)){
          firestore.collection("users").document(auth.uid!!)
              .collection("info")
              .document()
              .set(userInfo)
              .addOnSuccessListener {

                  viewModelScope.launch {
                      _addNewInfo.emit(Resource.Success(userInfo))
                  }

              }.addOnFailureListener {
                  viewModelScope.launch {
                      _addNewInfo.emit(Resource.Error(it.message.toString()))
                  }
              }
      }else {
                viewModelScope.launch {
                    _error.emit("All fields are required!")
                }
      }


    }

    private fun validateInput(userInfo: UserInfo): Boolean {
        return userInfo.addressTitle.trim().isNotEmpty() &&
                userInfo.city.trim().isNotEmpty() &&
                userInfo.phone.trim().isNotEmpty() &&
                userInfo.street.trim().isNotEmpty() &&
                userInfo.fullName.trim().isNotEmpty()
    }
}