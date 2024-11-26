package com.mdinterior.mdinterior.presentation.viewModels.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.fragment.client.home.AllProjects
import com.mdinterior.mdinterior.presentation.fragment.client.service.Services
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientServicesViewModel @Inject constructor(private val databaseReference: DatabaseReference) :
    ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    var servicesData = MutableLiveData<FireBaseEvents>()

    fun getHomeRecentWorkData() {
        servicesData.postValue(FireBaseEvents.FirebaseLoading)
        databaseReference.child("data").child("services").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    servicesData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            JsonConvertor.toJason(snapshot.getValue(Services::class.java)),
                            false
                        )
                    )
                } else {
                    servicesData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            snapshot.value.toString(),
                            true
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                servicesData.postValue(
                    FireBaseEvents.FirebaseError(error.message)
                )
            }
        })
    }


}