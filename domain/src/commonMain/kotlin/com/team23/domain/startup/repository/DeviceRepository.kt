package com.team23.domain.startup.repository

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface DeviceRepository {

    @OptIn(ExperimentalUuidApi::class)
    suspend fun createDeviceIdAndStoreIt(userId: Uuid): Result<Unit>


    @OptIn(ExperimentalUuidApi::class)
    fun getDeviceId(): Result<Uuid>
}
