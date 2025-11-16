package com.team23.data.auth

import com.team23.data.datastore.SetDataStore
import com.team23.domain.startup.repository.AuthRepository
import kotlin.uuid.ExperimentalUuidApi

class AuthRepositoryImpl(
    private val setDataStore: SetDataStore,
    private val authApi: AuthApi,
) : AuthRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun registerAndStoreUserInfo(
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
            is AuthRegisterResponse.Success -> storeUserInfo(response.playerId.toString(), username)
            is AuthRegisterResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun loginAndStoreUserInfo(username: String, password: String): Result<Unit> = runCatching {
        val request = AuthRequest(username = username, password = password)
        val response = authApi.signin(request)
        when (response) {
            is AuthSignResponse.Success -> storeUserInfo(response.playerId.toString(), username)
            is AuthSignResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    private suspend fun storeUserInfo(userId: String, username: String) {
        setDataStore.setValue(SetDataStore.USER_ID_KEY, userId)
        setDataStore.setValue(SetDataStore.USERNAME_KEY, username)
    }
}
