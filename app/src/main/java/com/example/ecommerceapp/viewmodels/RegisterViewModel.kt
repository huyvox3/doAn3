package com.example.ecommerceapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.utils.*
import com.example.ecommerceapp.utils.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth,
    private val firestore:FirebaseFirestore
):ViewModel(){


    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    private val _validation = Channel<RegisterFieldsState>()

     val register: Flow<Resource<User>> = _register
    val validation = _validation.receiveAsFlow()

    fun createNativeAccount(user: User,password:String){
       if ( isRegisterable(user, password)){

        runBlocking {
            _register.emit(Resource.Loading())
        }
        firebaseAuth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener {
                it.user?.let{
                    saveUserInfo(it.uid,user)

                }
            }
            .addOnFailureListener{

                _register.value = Resource.Error(it.message.toString())

            }

       }else{
           val registerFieldsState = RegisterFieldsState(
               validateEmail(user.email),
               validatePassword(password)
           )
           runBlocking {
               _validation.send(registerFieldsState)
           }
       }
    }

    private fun saveUserInfo(userID:String, user: User ) {
        firestore.collection(USER_COLLECTION)
            .document(userID)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)

            }.addOnFailureListener{
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun isRegisterable(user: User, password: String):Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        return   emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

    }
}


