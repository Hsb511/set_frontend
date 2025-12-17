package com.team23.data.datastore

import kotlinx.coroutines.flow.Flow

interface SetDataStore {

    suspend fun setValue(key: String, value: String)
    suspend fun getValue(key: String): String?
    fun getFlowValue(key: String): Flow<String?>
    suspend fun clear()

    companion object {
        internal const val DATA_STORE_FILE_NAME = "team23.set.preferences_pb"
        internal const val USER_ID_KEY = "userId"
        internal const val USERNAME_KEY = "username"
        internal const val PASSWORD_KEY = "password"
        internal const val SESSION_TOKEN_KEY = "sessionToken"
        internal const val IS_GUEST_KEY = "isGuest"

        internal const val PREFERENCE_CARD_ORIENTATION_KEY = "preference.cardOrientation"
        internal const val PREFERENCE_FORCE_DARK_MODE_KEY = "preference.forceDarkMode"
        internal const val PREFERENCE_FORCE_LIGHT_MODE_KEY = "preference.forceLightMode"
        internal const val PREFERENCE_DISABLE_ANIMATION_KEY = "preference.disableAnimation"
    }
}
