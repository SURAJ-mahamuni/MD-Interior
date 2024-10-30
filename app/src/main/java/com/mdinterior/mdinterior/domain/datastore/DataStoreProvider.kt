package com.mdinterior.mdinterior.domain.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreProvider @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")
    private var dataStoreInstance: DataStore<Preferences>? = null

    fun getDataStore(): DataStore<Preferences> {
        dataStoreInstance = context.dataStore
        return dataStoreInstance as DataStore<Preferences>
    }
}