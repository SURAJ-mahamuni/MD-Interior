package com.mdinterior.mdinterior.presentation.viewModels.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdinterior.mdinterior.domain.datastore.DataStoreManager
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants.USER_DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientMainViewModel @Inject constructor(private val dataStoreManager: DataStoreManager) :
    ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    fun setTitle() {
        viewModelScope.launch(Dispatchers.IO) {
            var username = ""
            async {
                username = dataStoreManager.getDataStoreValue(USER_DATA).toString()
            }.await()
            _appEvent.postValue(AppEvent.Other(username))
        }
    }
}