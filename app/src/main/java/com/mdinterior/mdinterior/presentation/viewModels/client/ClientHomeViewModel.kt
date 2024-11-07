package com.mdinterior.mdinterior.presentation.viewModels.client

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
import com.mdinterior.mdinterior.presentation.fragment.client.home.AllProjects
import com.mdinterior.mdinterior.presentation.fragment.client.home.HomeData
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientHomeViewModel @Inject constructor(private val databaseReference: DatabaseReference) :
    ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    var homeData = MutableLiveData<FireBaseEvents>()

    var homeDataRecentWorkData = MutableLiveData<FireBaseEvents>()

    var homeDataUI = HomeData()

    fun openProject() {
        _appEvent.postValue(AppEvent.NavigateFragmentEvent(R.id.projectsFragment))
    }

    fun getHomeData() {
        homeData.postValue(FireBaseEvents.FirebaseLoading)
        databaseReference.child("data").child("users").child("userId_1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.e("homeViewModel",snapshot.getValue(HomeData::class.java).toString())
                    homeData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            JsonConvertor.toJason(snapshot.getValue(HomeData::class.java)),
                            false
                        )
                    )
                } else {
                    homeData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            snapshot.value.toString(),
                            true
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                homeData.postValue(
                    FireBaseEvents.FirebaseError(error.message)
                )
            }
        })
    }
    fun getHomeRecentWorkData() {
        homeDataRecentWorkData.postValue(FireBaseEvents.FirebaseLoading)
        databaseReference.child("data").child("recentWork").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    homeDataRecentWorkData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            JsonConvertor.toJason(snapshot.getValue(AllProjects::class.java)),
                            false
                        )
                    )
                } else {
                    homeDataRecentWorkData.postValue(
                        FireBaseEvents.FirebaseSuccess(
                            snapshot.value.toString(),
                            true
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                homeDataRecentWorkData.postValue(
                    FireBaseEvents.FirebaseError(error.message)
                )
            }
        })
    }


}