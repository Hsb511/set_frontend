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
        isGuest: Boolean,
    ): Result<Unit> = runCatching {
        val request = AuthRequest(
            username = username,
            password = password,
            name = firstname.orEmpty(),
            surname = lastname.orEmpty()
        )
        when (val response = authApi.register(request)) {
            is AuthRegisterResponse.Success -> loginAndStoreUserInfo(username, password, isGuest)
            is AuthRegisterResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    override suspend fun loginAndStoreUserInfo(username: String, password: String, isGuest: Boolean): Result<Unit> = runCatching {
        val request = AuthRequest(username = username, password = password)
        when (val response = authApi.signin(request)) {
            is AuthSignResponse.Success -> {
                setDataStore.setValue(SetDataStore.USER_ID_KEY, response.playerId.toString())
                setDataStore.setValue(SetDataStore.USERNAME_KEY, username)
                setDataStore.setValue(SetDataStore.PASSWORD_KEY, password)
                setDataStore.setValue(SetDataStore.SESSION_TOKEN_KEY, response.sessionToken.toString())
                setDataStore.setValue(SetDataStore.IS_GUEST_KEY, isGuest.toString())
            }
            is AuthSignResponse.Failure -> throw IllegalArgumentException(response.error)
        }
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val sessionToken = getCachedSessionToken()
        when (val response = authApi.signOut(sessionToken)) {
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
