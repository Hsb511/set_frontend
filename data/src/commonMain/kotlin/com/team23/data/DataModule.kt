package com.team23.data

import com.team23.data.admin.AdminApi
import com.team23.data.admin.AdminApiImpl
import com.team23.data.admin.AdminRepositoryImpl
import com.team23.data.auth.AuthApi
import com.team23.data.auth.AuthApiImpl
import com.team23.data.auth.AuthRepositoryImpl
import com.team23.data.card.CardDataMapper
import com.team23.data.game.GameApi
import com.team23.data.game.GameApiImpl
import com.team23.data.game.GameRepositoryImpl
import com.team23.data.user.UserRepositoryImpl
import com.team23.domain.admin.AdminRepository
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.startup.repository.UserRepository
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    includes(platformModule())
    single<HttpClient> { createHttpClient() }

    factoryOf(::CardDataMapper)

    single { AdminApiImpl(get()) as AdminApi }
    single { AuthApiImpl(get()) as AuthApi }
    single { GameApiImpl(get()) as GameApi }

    single { AdminRepositoryImpl(get()) as AdminRepository }
    single { AuthRepositoryImpl(get(), get()) as AuthRepository }
    single { GameRepositoryImpl(get(), get(), get()) as GameRepository }
    single { UserRepositoryImpl(get()) as UserRepository }
}

internal expect fun platformModule(): Module
internal expect fun createHttpClient(): HttpClient