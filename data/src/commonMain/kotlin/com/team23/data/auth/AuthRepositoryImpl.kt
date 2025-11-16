package com.team23.data.auth

import com.team23.data.datastore.SetDataStore
import com.team23.domain.startup.repository.AuthRepository
import kotlin.uuid.ExperimentalUuidApi

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
        val request = AuthRequest(
            username = username,
            password = password,
            name = firstname.orEmpty(),
            surname = lastname.orEmpty()
        )
        val response = authApi.register(request)
        when (response) {
            is AuthRegisterResponse.Success -> storeUserId(response.playerId.toString())
            is AuthRegisterResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun loginAndStoreUserId(username: String, password: String): Result<Unit> = runCatching {
        val request = AuthRequest(username = username, password = password)
        val response = authApi.signin(request)
        when (response) {
            is AuthSignResponse.Success -> storeUserId(response.playerId.toString())
            is AuthSignResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    private suspend fun storeUserId(userId: String) {
        setDataStore.setValue(SetDataStore.USER_ID_KEY, userId)
    }
}
