package com.team23.domain.startup.repository

import com.team23.domain.settings.Preference
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserRepository {

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getUserInfo(): Result<Pair<Uuid, String>>
    suspend fun getUserPreference(preference: Preference): Boolean?
    suspend fun setUserPreference(preference: Preference, value: Boolean?)
}
