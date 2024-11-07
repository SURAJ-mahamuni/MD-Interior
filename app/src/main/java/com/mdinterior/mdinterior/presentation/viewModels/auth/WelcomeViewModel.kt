package com.mdinterior.mdinterior.presentation.viewModels.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    fun loginClick() {
        _appEvent.postValue(AppEvent.NavigateFragmentEvent(R.id.loginFragment))
    }

    fun registerClick() {
        _appEvent.postValue(AppEvent.NavigateFragmentEvent(R.id.registerFragment))
    }


}