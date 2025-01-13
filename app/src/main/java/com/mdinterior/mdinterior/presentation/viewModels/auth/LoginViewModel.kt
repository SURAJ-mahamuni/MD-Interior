package com.mdinterior.mdinterior.presentation.viewModels.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.domain.datastore.DataStoreManager
import com.mdinterior.mdinterior.domain.validation.ValidationData
import com.mdinterior.mdinterior.domain.validation.ValidationEvent
import com.mdinterior.mdinterior.domain.validation.ValidationManager
import com.mdinterior.mdinterior.domain.validation.ValidationType
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants.IS_LOGGED_IN
import com.mdinterior.mdinterior.presentation.helper.Constants.USER_DATA
import com.mdinterior.mdinterior.presentation.helper.Constants.USER_ID
import com.mdinterior.mdinterior.presentation.helper.Constants.USER_TYPE
import com.mdinterior.mdinterior.presentation.model.LoginUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    val loginUiData = MutableLiveData(LoginUiData())

    fun onBackClick() {
        _appEvent.postValue(AppEvent.NavigateBackEvent(""))
    }

    private fun saveUserDataInDataStore(
        documentSnapshot: DocumentSnapshot,
        authResult: AuthResult
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            async {
                dataStoreManager.setDataInDataStore(
                    USER_DATA,
                    documentSnapshot.getString("username") ?: ""
                )
            }.await()
            async {
                dataStoreManager.setDataInDataStore(
                    USER_TYPE,
                    documentSnapshot.getString("isAdmin") ?: ""
                )
            }.await()
            dataStoreManager.setDataInDataStore(
                USER_ID,
                authResult.user?.uid ?: ""
            )
        }
    }

    fun setLoggedIn() {
        viewModelScope.launch {
            dataStoreManager.setDataInDataStore(IS_LOGGED_IN, "true")
        }
    }

    fun loginClick() {
        _appEvent.postValue(AppEvent.Other("show_progressBar"))
        ValidationManager().validateFields(
            ValidationData(
                loginUiData.value?.email, ValidationType.EMAIL
            ), ValidationData(loginUiData.value?.password, ValidationType.PASSWORD)
        ).let {
            when (it) {
                ValidationEvent.Continue -> {
                    firebaseAuth.signInWithEmailAndPassword(
                        loginUiData.value?.email ?: "",
                        loginUiData.value?.password ?: ""
                    ).apply {
                        addOnSuccessListener { authResult ->
                            Log.e("user", "login")
                            authResult.user?.uid
                            checkUserData(authResult)
                        }
                        addOnFailureListener {
                            Log.e("user", it.message, it)
                            _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                            viewModelScope.launch(Dispatchers.Default) {
                                async { _appEvent.postValue(AppEvent.ToastEvent(R.string.login_failed)) }.await()
                            }
                        }
                    }
                }

                is ValidationEvent.ErrorMessage -> {
                    _appEvent.postValue(AppEvent.ToastEvent(it.message))
                }
            }
        }
    }

    private fun checkUserData(authResult: AuthResult) {
        val df = firebaseFireStore.collection("Users").document(authResult.user?.uid ?: "")
        df.get().apply {
            addOnSuccessListener {
                val isAdmin = it.getString("isAdmin")
                Log.e("user", isAdmin.toString())
                saveUserDataInDataStore(it, authResult)
                if (isAdmin == "0") {
                    _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                    viewModelScope.launch(Dispatchers.Default) {
                        async { _appEvent.postValue(AppEvent.NavigateFragmentEvent(0)) }.await()
                    }
                } else if (isAdmin == "1") {
                    _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                    viewModelScope.launch(Dispatchers.Default) {
                        async { _appEvent.postValue(AppEvent.NavigateFragmentEvent(1)) }.await()
                    }

                }
            }
            addOnFailureListener {
                _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                viewModelScope.launch(Dispatchers.Default) {
                    async { _appEvent.postValue(AppEvent.ToastEvent(R.string.login_failed)) }.await()
                }
            }
        }
    }


}