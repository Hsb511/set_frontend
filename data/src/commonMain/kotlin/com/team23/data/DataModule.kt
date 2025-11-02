package com.team23.data

import com.team23.data.auth.AuthRepositoryImpl
import com.team23.data.device.DeviceRepositoryImpl
import com.team23.data.game.GameRepositoryImpl
import com.team23.data.user.UserRepositoryImpl
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.startup.repository.DeviceRepository
import com.team23.domain.startup.repository.UserRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    includes(platformModule())

    single { AuthRepositoryImpl() as AuthRepository }
    single { DeviceRepositoryImpl() as DeviceRepository }
    single { GameRepositoryImpl() as GameRepository }
    single { UserRepositoryImpl() as UserRepository }
}

internal expect fun platformModule(): Module
