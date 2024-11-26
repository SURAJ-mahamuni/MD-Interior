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
class ClientProjectViewModel @Inject constructor(private val databaseReference: DatabaseReference) :
    ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    var projectsData = MutableLiveData<FireBaseEvents>()

    fun getProjectsData() {
        projectsData.postValue(FireBaseEvents.FirebaseLoading)
        databaseReference.child("data").child("allProjects").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    projectsData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            JsonConvertor.toJason(snapshot.getValue(AllProjects::class.java)),
                            false
                        )
                    )
                } else {
                    projectsData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            snapshot.value.toString(),
                            true
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                projectsData.postValue(
                    FireBaseEvents.FirebaseError(error.message)
                )
            }
        })
    }



}