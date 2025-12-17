package com.team23.domain.startup.repository

import kotlin.uuid.ExperimentalUuidApi

interface AuthRepository {

    suspend fun registerAndStoreUserInfo(
        username: String,
        password: String,
        firstname: String? = null,
        lastname: String? = null,
        isGuest: Boolean = false,
    ): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun loginAndStoreUserInfo(
        username: String,
        password: String,
        isGuest: Boolean = false,
    ): Result<Unit>

    suspend fun logout(): Result<Unit>
}