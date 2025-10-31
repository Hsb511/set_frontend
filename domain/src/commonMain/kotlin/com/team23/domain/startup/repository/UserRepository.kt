package com.team23.domain.startup.repository

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserRepository {

    @OptIn(ExperimentalUuidApi::class)
    fun getUserId(): Result<Uuid>
}
