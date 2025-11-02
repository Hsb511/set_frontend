package com.team23.domain.startup.repository

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface DeviceRepository {

    @OptIn(ExperimentalUuidApi::class)
    suspend fun createDeviceIdAndStoreIt(): Result<Unit>


    @OptIn(ExperimentalUuidApi::class)
    suspend fun getDeviceId(): Result<Uuid>
}
