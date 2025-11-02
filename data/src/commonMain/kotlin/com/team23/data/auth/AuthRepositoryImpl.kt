package com.team23.data.auth

import com.team23.data.datastore.SetDataStore
import com.team23.domain.startup.repository.AuthRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthRepositoryImpl(
    private val setDataStore: SetDataStore,
) : AuthRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun registerAndStoreUserId(): Result<Unit> = runCatching {
        val newUserId = Uuid.random().toString()
        setDataStore.setValue(SetDataStore.USER_ID_KEY, newUserId)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun loginAndStoreUserId(): Result<Unit> = runCatching {
        val newUserId = Uuid.random().toString()
        setDataStore.setValue(SetDataStore.USER_ID_KEY, newUserId)
    }
}