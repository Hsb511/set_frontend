package com.team23.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team23.data.datastore.SetDataStore.Companion.DATA_STORE_FILE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class SetDataStoreImpl() : SetDataStore {

    override suspend fun setValue(key: String, value: String) {
        if (isDataStoreInitialized()) {
            dataStore.edit { preferences ->
                val prefKey = stringPreferencesKey(key)
                preferences[prefKey] = value
            }
        }
    }

    override suspend fun getValue(key: String): String? {
        if (!isDataStoreInitialized()) return null

        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences -> preferences[prefKey] }.firstOrNull()
    }

    override fun getFlowValue(key: String): Flow<String?> {
        if (!isDataStoreInitialized()) return emptyFlow()

        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences -> preferences[prefKey] }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private lateinit var dataStore: DataStore<Preferences>
        private fun isDataStoreInitialized(): Boolean = ::dataStore.isInitialized

        fun injectContext(context: Context) {
            if (!isDataStoreInitialized()) {
                val appContext = context.applicationContext
                dataStore = PreferenceDataStoreFactory.createWithPath(
                    produceFile = {
                        appContext.filesDir
                            .resolve(DATA_STORE_FILE_NAME)
                            .absolutePath
                            .toPath()
                    }
                )
            }
        }
    }
}
