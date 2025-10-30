package com.team23.domain.startup.usecase

interface SignInUseCase {
    suspend fun invoke(): Result<Unit>
}

class SignInUseCaseImpl: SignInUseCase {

    override suspend fun invoke(): Result<Unit> = Result.success(Unit)
}
