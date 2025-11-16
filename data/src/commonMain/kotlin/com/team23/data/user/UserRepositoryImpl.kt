package com.team23.data.user

import com.team23.data.datastore.SetDataStore
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
}
