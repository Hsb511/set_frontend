package com.team23.data.auth

import com.team23.data.datastore.SetDataStore
import com.team23.domain.startup.repository.AuthRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthRepositoryImpl(
    private val setDataStore: SetDataStore,
    private val authApi: AuthApi,
) : AuthRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun registerAndStoreUserId(
        username: String,
        password: String,
        firstname: String?,
        lastname: String?,
    ): Result<Unit> = runCatching {
        val request = AuthRegisterRequest(
            username = username,
            password = password,
            name = firstname.orEmpty(),
            surname = lastname.orEmpty()
        )
        val response = authApi.register(request)
        when (response) {
            is AuthRegisterResponse.Success -> {
                val newUserId = response.playerId.toString()
                setDataStore.setValue(SetDataStore.USER_ID_KEY, newUserId)
            }
            is AuthRegisterResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun loginAndStoreUserId(username: String, password: String): Result<Unit> = runCatching {
        TODO()
        // setDataStore.setValue(SetDataStore.USER_ID_KEY, userId.toString())
    }
}