package com.team23.data.auth

import com.team23.data.datastore.SetDataStore
import com.team23.domain.startup.repository.AuthRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class AuthRepositoryImpl(
    private val setDataStore: SetDataStore,
    private val authApi: AuthApi,
) : AuthRepository {

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
            is AuthRegisterResponse.Success -> loginAndStoreUserInfo(username, password)
            is AuthRegisterResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    override suspend fun loginAndStoreUserInfo(username: String, password: String): Result<Unit> = runCatching {
        val request = AuthRequest(username = username, password = password)
        val response = authApi.signin(request)
        when (response) {
            is AuthSignResponse.Success -> {
                setDataStore.setValue(SetDataStore.USER_ID_KEY, response.playerId.toString())
                setDataStore.setValue(SetDataStore.USERNAME_KEY, username)
                setDataStore.setValue(SetDataStore.PASSWORD_KEY, password)
                setDataStore.setValue(SetDataStore.SESSION_TOKEN_KEY, response.sessionToken.toString())
            }
            is AuthSignResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val sessionToken = getCachedSessionToken()
        val response = authApi.signOut(sessionToken)
        when (response) {
            is AuthSignOutResponse.Success -> setDataStore.clear()
            is AuthSignOutResponse.Failure -> Exception(response.error)
        }
    }

    private suspend fun getCachedSessionToken(): Uuid {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        return Uuid.parse(cachedSessionToken)
    }
}
