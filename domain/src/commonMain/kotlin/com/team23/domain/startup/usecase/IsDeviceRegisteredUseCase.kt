package com.team23.domain.startup.usecase

interface IsDeviceRegisteredUseCase {
    suspend fun invoke(): Boolean
}

class IsDeviceRegisteredUseCaseImpl: IsDeviceRegisteredUseCase {

   override suspend fun invoke(): Boolean = false
}
