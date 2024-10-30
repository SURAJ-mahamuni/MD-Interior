package com.mdinterior.mdinterior.domain.firebase

import com.google.firebase.auth.FirebaseAuth

object FireBaseInstance {

    private lateinit var firebaseAut: FirebaseAuth


    fun getAuthInstants(): FirebaseAuth {
        firebaseAut = FirebaseAuth.getInstance()
        return firebaseAut
    }

}