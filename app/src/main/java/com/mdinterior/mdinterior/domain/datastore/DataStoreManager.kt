package com.mdinterior.mdinterior.domain.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStoreProvider: DataStoreProvider
) {
    suspend fun setDataInDataStore(key: String, value: String) {
        dataStoreProvider.getDataStore().edit { setting ->
            setting[stringPreferencesKey(key)] = value
            Log.e("stringPreferencesKey", setting[stringPreferencesKey(key)].toString())
        }
    }

    suspend fun getDataStoreValue(key: String): String? {
        Log.e(
            "dataStoreProvider",
            dataStoreProvider.getDataStore().data.asLiveData().toString()
        )
        return dataStoreProvider.getDataStore().data.first()[stringPreferencesKey(key)]
    }

    suspend fun clearDataStore(
        context: Context
    ) {
        val dataStore = dataStoreProvider.getDataStore()
        dataStore.edit {
            it.clear()
        }
    }

}