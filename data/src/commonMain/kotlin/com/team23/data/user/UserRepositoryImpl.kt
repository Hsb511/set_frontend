package com.team23.data.user

import com.team23.domain.startup.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepositoryImpl: UserRepository {

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserId(): Result<Uuid> {
        return Result.success(Uuid.random())
    }
}