package com.team23.data.device

import com.team23.domain.startup.repository.DeviceRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeviceRepositoryImpl : DeviceRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createDeviceIdAndStoreIt(userId: Uuid): Result<Unit> {
        return Result.success(Unit)
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getDeviceId(): Result<Uuid> {
        return Result.success(Uuid.random())
    }
}