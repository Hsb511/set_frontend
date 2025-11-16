package com.team23.data

import com.team23.data.auth.AuthApi
import com.team23.data.auth.AuthApiImpl
import com.team23.data.auth.AuthRepositoryImpl
import com.team23.data.game.GameRepositoryImpl
import com.team23.data.user.UserRepositoryImpl
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.startup.repository.UserRepository
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    includes(platformModule())
    single<HttpClient> { createHttpClient() }

    single { AuthApiImpl(get()) as AuthApi }

    single { AuthRepositoryImpl(get(), get()) as AuthRepository }
    single { GameRepositoryImpl() as GameRepository }
    single { UserRepositoryImpl(get()) as UserRepository }
}

internal expect fun platformModule(): Module
internal expect fun createHttpClient(): HttpClient