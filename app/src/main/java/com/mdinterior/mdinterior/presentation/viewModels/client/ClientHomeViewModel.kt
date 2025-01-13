package com.mdinterior.mdinterior.presentation.viewModels.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.domain.datastore.DataStoreManager
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.fragment.client.home.AllProjects
import com.mdinterior.mdinterior.presentation.fragment.client.home.HomeData
import com.mdinterior.mdinterior.presentation.fragment.client.home.User
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientHomeViewModel @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val dataStoreManager: DataStoreManager,
) :
    ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    var homeData = MutableLiveData<FireBaseEvents>()

    var homeDataRecentWorkData = MutableLiveData<FireBaseEvents>()

    fun openProject() {
        _appEvent.postValue(AppEvent.NavigateFragmentEvent(R.id.projectsFragment))
    }

    fun getHomeData() {
        homeData.postValue(FireBaseEvents.FirebaseLoading)
        databaseReference.child("data").child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Log.e("homeViewModel", snapshot.getValue(HomeData::class.java).toString())
                        viewModelScope.launch(Dispatchers.IO) {
                            var USER_ID = ""
                            async {
                                USER_ID = dataStoreManager.getDataStoreValue(
                                    Constants.USER_ID
                                ).toString()
                                Log.e("Saved USER ID", USER_ID)
                            }.await()
                            Log.e("all users",snapshot.getValue(HomeData::class.java)?.user.toString())
                            val data =
                                snapshot.getValue(HomeData::class.java)?.user?.filter { it.userId == USER_ID }
                            if (!data.isNullOrEmpty()) {
                                data[0].let { user ->
                                    Log.e("userData", user.toString() + " user id " + USER_ID)
                                    homeData.postValue(
                                        FireBaseEvents.FirebaseSuccess(
                                            JsonConvertor.toJason(user),
                                            false
                                        )
                                    )
                                }

                            } else {
                                _appEvent.postValue(AppEvent.ToastEvent(R.string.user_id_not_match))
                            }

                        }
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
        databaseReference.child("data").child("recentWork")
            .addValueEventListener(object : ValueEventListener {
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