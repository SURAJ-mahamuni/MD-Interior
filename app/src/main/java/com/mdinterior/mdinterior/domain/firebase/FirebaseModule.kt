package com.mdinterior.mdinterior.domain.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mdinterior.mdinterior.domain.firebase.FireBaseInstance.getAuthInstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun getFirebaseInstance(): FirebaseAuth {
        return getAuthInstants()
    }

    @Provides
    @Singleton
    fun getFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}