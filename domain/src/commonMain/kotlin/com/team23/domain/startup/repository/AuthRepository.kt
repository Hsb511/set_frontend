package com.team23.domain.startup.repository

interface AuthRepository {

    suspend fun registerAndStoreUserId(): Result<Unit>

    suspend fun loginAndStoreUserId(): Result<Unit>
}