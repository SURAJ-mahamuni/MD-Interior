package com.mdinterior.mdinterior.presentation.viewModels.startScreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdinterior.mdinterior.domain.datastore.DataStoreManager
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants
import com.mdinterior.mdinterior.presentation.helper.Constants.IS_LOGGED_IN
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val dataStoreManager: DataStoreManager,
) :
    ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent


    fun checkNavigation() {
        viewModelScope.launch(Dispatchers.IO) {
            var isLoggedIn = ""
            async {
                isLoggedIn = dataStoreManager.getDataStoreValue(
                    IS_LOGGED_IN
                ).toString()
            }.await()
            if (isLoggedIn == "true"){
                checkUserType()
            }else{
                _appEvent.postValue(AppEvent.NavigateActivityEvent("Welcome"))
            }
        }
    }

    private fun checkUserType() {
        viewModelScope.launch(Dispatchers.IO) {
            var userType = ""
            async {
                userType = dataStoreManager.getDataStoreValue(
                    Constants.USER_TYPE
                ).toString()
            }.await()
            if (userType =="0"){
                _appEvent.postValue(AppEvent.NavigateActivityEvent("Client"))
            }else if (userType == "1"){
                _appEvent.postValue(AppEvent.NavigateActivityEvent("Admin"))
            }else{
                _appEvent.postValue(AppEvent.NavigateActivityEvent("Welcome"))
            }
        }

    }

}