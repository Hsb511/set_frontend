package com.team23.data.auth

import com.team23.domain.startup.repository.AuthRepository

class AuthRepositoryImpl: AuthRepository {
    override suspend fun registerAndStoreUserId(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun loginAndStoreUserId(): Result<Unit> {
        return Result.success(Unit)
    }
}