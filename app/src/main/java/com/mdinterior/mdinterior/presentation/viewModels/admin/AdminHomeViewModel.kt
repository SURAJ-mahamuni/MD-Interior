package com.mdinterior.mdinterior.presentation.viewModels.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.fragment.client.home.HomeData
import com.mdinterior.mdinterior.presentation.fragment.client.home.User
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants.CHILD1
import com.mdinterior.mdinterior.presentation.helper.Constants.CHILD2
import com.mdinterior.mdinterior.presentation.helper.Constants.PARENT
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val databaseReference: DatabaseReference,
) :
    ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    var userList = MutableLiveData<FireBaseEvents>()

    fun openAppProject() {
        _appEvent.postValue(AppEvent.NavigateFragmentEvent(R.id.addProjectFragment))
    }

    fun getUserList() {
        userList.postValue(FireBaseEvents.FirebaseLoading)
        databaseReference.child(PARENT).child(CHILD1).child(CHILD2)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userListTemp = mutableListOf<User>()
                        for (userSnapshot in snapshot.children) {
                            Log.e("user", userSnapshot.toString())
                            // Convert each child into a User object
                            val user = userSnapshot.getValue(User::class.java)
                            if (user != null) {
                                userListTemp.add(user)
                            }
                            userList.postValue(
                                FireBaseEvents.FirebaseSuccess(
                                    JsonConvertor.toJason(HomeData(userListTemp)),
                                    false
                                )
                            )
                        }

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