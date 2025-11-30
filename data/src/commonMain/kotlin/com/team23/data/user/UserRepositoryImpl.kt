package com.team23.data.user

import com.team23.data.datastore.SetDataStore
import com.team23.domain.settings.Preference
import com.team23.domain.startup.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepositoryImpl(
    private val setDataStore: SetDataStore
): UserRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserInfo(): Result<Pair<Uuid, String>> = runCatching {
        val rawUserId = setDataStore.getValue(SetDataStore.USER_ID_KEY)
        requireNotNull(rawUserId)
        val username = setDataStore.getValue(SetDataStore.USERNAME_KEY)
        requireNotNull(username)
        Uuid.parse(rawUserId) to username
    }

    override suspend fun getUserPreference(preference: Preference): Boolean? {
        val key = mapToKeyDataStore(preference)
        return setDataStore.getValue(key)?.toBoolean()
    }

    override suspend fun setUserPreference(preference: Preference, value: Boolean?) {
        val key = mapToKeyDataStore(preference)
        setDataStore.setValue(key, value?.toString().orEmpty())
    }

    private fun mapToKeyDataStore(preference: Preference): String = when(preference) {
        Preference.CardOrientation -> SetDataStore.PREFERENCE_CARD_ORIENTATION_KEY
        Preference.ForceLightMode -> SetDataStore.PREFERENCE_FORCE_LIGHT_MODE_KEY
        Preference.ForceDarkMode -> SetDataStore.PREFERENCE_FORCE_DARK_MODE_KEY
    }
}
