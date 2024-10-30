package com.mdinterior.mdinterior.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.domain.validation.ValidationData
import com.mdinterior.mdinterior.domain.validation.ValidationEvent
import com.mdinterior.mdinterior.domain.validation.ValidationManager
import com.mdinterior.mdinterior.domain.validation.ValidationType
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.model.RegisterUIData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
) : ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    var registerUiData = MutableLiveData(RegisterUIData())

    fun registerClick() {
        _appEvent.postValue(AppEvent.Other("show_progressBar"))
        if (registerUiData.value?.password == registerUiData.value?.confirmPassword) {
            ValidationManager().validateFields(
                ValidationData(
                    registerUiData.value?.emailId, ValidationType.EMAIL
                ), ValidationData(registerUiData.value?.password, ValidationType.PASSWORD)
            ).let {
                when (it) {
                    ValidationEvent.Continue -> {
                        firebaseAuth.createUserWithEmailAndPassword(
                            registerUiData.value?.emailId ?: "",
                            registerUiData.value?.password ?: ""
                        ).apply {
                            addOnSuccessListener { authResult ->
                                Log.e("user", "register")
                                addUserData(authResult)
                            }
                            addOnFailureListener {
                                Log.e("user", it.message, it)
                                viewModelScope.launch(Dispatchers.Default) {
                                    async { _appEvent.postValue(AppEvent.ToastEvent(R.string.an_issue_occurred_on_the_server_side)) }.await()
                                }
                            }
                        }
                    }

                    is ValidationEvent.ErrorMessage -> {
                        _appEvent.postValue(AppEvent.ToastEvent(it.message))
                    }
                }
            }
        } else {
            _appEvent.postValue(AppEvent.ToastEvent(R.string.password_and_confirm_password_do_not_match))
        }

    }

    private fun addUserData(authResult: AuthResult) {
        val df = firebaseFireStore.collection("Users").document(authResult.user?.uid ?: "")
        var map = HashMap<String, Any>()
        map.apply {
            put("username", registerUiData.value?.username ?: "")
            put("isAdmin", "0")
        }
        df.set(map).apply {
            addOnSuccessListener {
                _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                viewModelScope.launch(Dispatchers.Default) {
                    async { _appEvent.postValue(AppEvent.ToastEvent(R.string.sign_up_completed_successfully))
                        _appEvent.postValue(AppEvent.NavigateBackEvent(""))}.await()
                }
            }
            addOnFailureListener {
                _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                viewModelScope.launch(Dispatchers.Default) {
                    async { _appEvent.postValue(AppEvent.ToastEvent(R.string.an_issue_occurred_on_the_server_side)) }.await()
                }
            }
        }
    }

    fun backClick() {
        _appEvent.postValue(AppEvent.NavigateBackEvent(""))
    }

}