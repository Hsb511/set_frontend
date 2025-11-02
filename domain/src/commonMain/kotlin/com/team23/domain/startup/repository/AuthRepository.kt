package com.team23.domain.startup.repository

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AuthRepository {

    suspend fun registerAndStoreUserId(): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun loginAndStoreUserId(userId: Uuid): Result<Unit>
}