package com.team23.data.user

import com.team23.data.datastore.SetDataStore
import com.team23.domain.startup.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepositoryImpl(
    private val setDataStore: SetDataStore
): UserRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserId(): Result<Uuid> = runCatching {
        val rawUserId = setDataStore.getValue(SetDataStore.USER_ID_KEY)
        requireNotNull(rawUserId)
        Uuid.parse(rawUserId)
    }
}
