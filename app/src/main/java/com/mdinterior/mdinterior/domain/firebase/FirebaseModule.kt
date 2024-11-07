package com.mdinterior.mdinterior.domain.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    @Provides
    @Singleton
    fun getFirebaseDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

}