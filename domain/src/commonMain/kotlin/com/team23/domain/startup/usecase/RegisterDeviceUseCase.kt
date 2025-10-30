package com.team23.domain.startup.usecase


interface RegisterDeviceUseCase {
    suspend fun invoke(): Result<Unit>
}

class RegisterDeviceUseCaseImpl: RegisterDeviceUseCase {

    override suspend fun invoke(): Result<Unit> = Result.success(Unit)
}
