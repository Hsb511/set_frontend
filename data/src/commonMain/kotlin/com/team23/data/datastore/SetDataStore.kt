package com.team23.data.datastore

interface SetDataStore {

    suspend fun setValue(key: String, value: String)
    suspend fun getValue(key: String): String?

    companion object {
        internal const val DATA_STORE_FILE_NAME = "team23.set.preferences_pb"
        internal const val USER_ID_KEY = "userId"
        internal const val USERNAME_KEY = "username"
        internal const val SESSION_TOKEN_KEY = "sessionToken"
    }
}
