package com.team23.domain.startup.usecase

interface SignUpUseCase {
    suspend fun invoke(): Result<Unit>
}

class SignUpUseCaseImpl : SignUpUseCase {

    override suspend fun invoke(): Result<Unit> = Result.success(Unit)
}
