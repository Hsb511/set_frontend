package com.team23.domain.startup.repository

import kotlin.uuid.ExperimentalUuidApi

interface AuthRepository {

    suspend fun registerAndStoreUserId(
        username: String,
        password: String,
        firstname: String?,
        lastname: String?,
    ): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun loginAndStoreUserId(
        username: String,
        password: String,
    ): Result<Unit>
}