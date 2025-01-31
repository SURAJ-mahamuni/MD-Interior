package com.mdinterior.mdinterior.presentation.viewModels.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.domain.validation.ValidationData
import com.mdinterior.mdinterior.domain.validation.ValidationEvent
import com.mdinterior.mdinterior.domain.validation.ValidationManager
import com.mdinterior.mdinterior.domain.validation.ValidationType
import com.mdinterior.mdinterior.presentation.fragment.client.home.User
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants.CHILD1
import com.mdinterior.mdinterior.presentation.helper.Constants.CHILD2
import com.mdinterior.mdinterior.presentation.helper.Constants.CLIENT_YES
import com.mdinterior.mdinterior.presentation.helper.Constants.PARENT
import com.mdinterior.mdinterior.presentation.helper.Constants.USER_AUTH_PARENT
import com.mdinterior.mdinterior.presentation.model.RegisterUIData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val databaseReference: DatabaseReference,
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
                                    async {
                                        _appEvent.postValue(
                                            AppEvent.ToastEventString(
                                                it.message ?: ""
                                            )
                                        )
                                    }.await()
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
        val df = firebaseFireStore.collection(USER_AUTH_PARENT).document(authResult.user?.uid ?: "")
        var map = HashMap<String, Any>()
        map.apply {
            put("username", registerUiData.value?.username ?: "")
            put("isAdmin", CLIENT_YES)
        }
        df.set(map).apply {
            addOnSuccessListener {
                saveUserValues(authResult.user?.uid)
            }
            addOnFailureListener {
                _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                viewModelScope.launch(Dispatchers.Default) {
                    async { _appEvent.postValue(AppEvent.ToastEvent(R.string.an_issue_occurred_on_the_server_side)) }.await()
                }
            }
        }
    }

    private fun saveUserValues(uid: String?) {
        FirebaseDatabase.getInstance().getReference(PARENT).child(CHILD1).child(CHILD2).let {
            it.child(uid ?: "").setValue(
                User(
                    userId = uid,
                    name = registerUiData.value?.username,
                    client = CLIENT_YES,
                    emailId = registerUiData.value?.emailId
                )
            ).addOnSuccessListener {
                _appEvent.postValue(AppEvent.Other("hide_progressBar"))
                viewModelScope.launch(Dispatchers.Default) {
                    async {
                        _appEvent.postValue(AppEvent.ToastEvent(R.string.sign_up_completed_successfully))
                    }.await()
                    _appEvent.postValue(AppEvent.NavigateBackEvent(""))
                }
                registerUiData.postValue(RegisterUIData())
            }.addOnFailureListener {
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