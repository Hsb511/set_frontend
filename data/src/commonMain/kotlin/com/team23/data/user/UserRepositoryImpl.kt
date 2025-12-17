package com.team23.data.user

import com.team23.data.datastore.SetDataStore
import com.team23.domain.settings.Preference
import com.team23.domain.user.UserInfo
import com.team23.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepositoryImpl(
    private val setDataStore: SetDataStore
): UserRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserInfo(): Result<UserInfo> = runCatching {
        val rawUserId = setDataStore.getValue(SetDataStore.USER_ID_KEY)
        requireNotNull(rawUserId)
        val username = setDataStore.getValue(SetDataStore.USERNAME_KEY)
        requireNotNull(username)
        val isGuest = setDataStore.getValue(SetDataStore.IS_GUEST_KEY)
        requireNotNull(isGuest)
        UserInfo(
            userId = Uuid.parse(rawUserId),
            username = username,
            isGuest = isGuest.toBoolean(),
        )
    }

    override fun getUserPreferenceAsFlow(preference: Preference): Flow<Boolean?> {
        val key = mapToKeyDataStore(preference)
        return setDataStore.getFlowValue(key).map { value -> value?.toBoolean() }
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
        Preference.CardPortrait -> SetDataStore.PREFERENCE_CARD_ORIENTATION_KEY
        Preference.ForceLightMode -> SetDataStore.PREFERENCE_FORCE_LIGHT_MODE_KEY
        Preference.ForceDarkMode -> SetDataStore.PREFERENCE_FORCE_DARK_MODE_KEY
        Preference.DisableAnimation -> SetDataStore.PREFERENCE_DISABLE_ANIMATION_KEY
    }
}
