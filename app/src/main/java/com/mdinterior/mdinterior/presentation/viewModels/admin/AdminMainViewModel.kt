package com.mdinterior.mdinterior.presentation.viewModels.admin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdinterior.mdinterior.domain.datastore.DataStoreManager
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    fun setTitle() {
        viewModelScope.launch(Dispatchers.IO) {
            var username = ""
            async {
                username = dataStoreManager.getDataStoreValue(Constants.USER_DATA).toString()
            }.await()
            _appEvent.postValue(AppEvent.Other(username))
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            async {
                dataStoreManager.clearDataStore(context = appContext)
            }.await()
            _appEvent.postValue(AppEvent.NavigateActivityEvent(Constants.LOGOUT))
        }
    }

}