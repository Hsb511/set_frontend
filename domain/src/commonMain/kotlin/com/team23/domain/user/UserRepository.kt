package com.team23.domain.user

import com.team23.domain.settings.Preference
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserRepository {

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getUserInfo(): Result<UserInfo>
    fun getUserPreferenceAsFlow(preference: Preference): Flow<Boolean?>
    suspend fun getUserPreference(preference: Preference): Boolean?
    suspend fun setUserPreference(preference: Preference, value: Boolean?)
}