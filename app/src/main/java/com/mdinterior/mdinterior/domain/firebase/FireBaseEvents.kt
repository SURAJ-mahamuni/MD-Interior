package com.mdinterior.mdinterior.domain.firebase

sealed class FireBaseEvents() {
    data class FirebaseError(val error : String) : FireBaseEvents()
    data class FirebaseSuccess(val data : String,val isEmpty : Boolean) : FireBaseEvents()
    data object FirebaseLoading : FireBaseEvents()
}