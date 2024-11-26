package com.mdinterior.mdinterior.presentation.viewModels.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.fragment.client.home.HomeData
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminUserViewModel @Inject constructor(
    private val databaseReference: DatabaseReference
) : ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    var userList = MutableLiveData<FireBaseEvents>()

    fun getUserList() {
        userList.postValue(FireBaseEvents.FirebaseLoading)
        databaseReference.child("data").child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userList.postValue(
                            FireBaseEvents.FirebaseSuccess(
                                JsonConvertor.toJason(snapshot.getValue(HomeData::class.java)),
                                false
                            )
                        )
                    } else {
                        userList.postValue(
                            FireBaseEvents.FirebaseSuccess(
                                snapshot.value.toString(),
                                true
                            )
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    userList.postValue(
                        FireBaseEvents.FirebaseError(error.message)
                    )
                }
            })
    }
}