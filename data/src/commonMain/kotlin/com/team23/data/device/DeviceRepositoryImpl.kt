package com.team23.data.device

import com.team23.data.datastore.SetDataStore
import com.team23.domain.startup.repository.DeviceRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeviceRepositoryImpl(
    private val setDataStore: SetDataStore,
): DeviceRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createDeviceIdAndStoreIt(): Result<Unit> = runCatching {
        val newDeviceId = Uuid.random().toString()
        setDataStore.setValue(SetDataStore.DEVICE_ID_KEY, newDeviceId)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getDeviceId(): Result<Uuid> = runCatching {
        val rawDeviceId = setDataStore.getValue(SetDataStore.DEVICE_ID_KEY)
        requireNotNull(rawDeviceId)
        Uuid.parse(rawDeviceId)
    }
}
