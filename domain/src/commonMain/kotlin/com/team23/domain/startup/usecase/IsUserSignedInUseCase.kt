package com.team23.domain.startup.usecase

interface IsUserSignedInUseCase {
    suspend fun invoke(): Boolean
}

class IsUserSignedInUseCaseImpl : IsUserSignedInUseCase {

    override suspend fun invoke(): Boolean = false
}
