package com.example.ecommerceapp.di


import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.ecommerceapp.utils.Constants.INTRODUCTION_SP
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore


    @Provides
    fun provideIntroductionSP(
        application: Application

    ) = application.getSharedPreferences(INTRODUCTION_SP,MODE_PRIVATE)

}