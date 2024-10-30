package com.mdinterior.mdinterior.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientHomeViewModel @Inject constructor() : ViewModel() {

    var _appEvent: MutableLiveData<AppEvent> = MutableLiveData()

    val appEvent: LiveData<AppEvent> get() = _appEvent

    fun openProject(){
        _appEvent.postValue(AppEvent.NavigateFragmentEvent(R.id.projectsFragment))
    }


}