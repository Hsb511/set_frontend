package com.team23.domain.startup.repository

import kotlin.uuid.ExperimentalUuidApi

interface AuthRepository {

    suspend fun registerAndStoreUserInfo(
        username: String,
        password: String,
        firstname: String?,
        lastname: String?,
    ): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun loginAndStoreUserInfo(
        username: String,
        password: String,
    ): Result<Unit>

    suspend fun logout(): Result<Unit>
}