package com.team23.domain

import com.team23.domain.game.statemachine.GameStateMachine
import com.team23.domain.game.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.domain.startup.usecase.IsDeviceRegisteredUseCase
import com.team23.domain.startup.usecase.IsDeviceRegisteredUseCaseImpl
import com.team23.domain.startup.usecase.IsUserSignedInUseCase
import com.team23.domain.startup.usecase.IsUserSignedInUseCaseImpl
import com.team23.domain.startup.usecase.RegisterDeviceUseCase
import com.team23.domain.startup.usecase.RegisterDeviceUseCaseImpl
import com.team23.domain.startup.usecase.SignInUseCase
import com.team23.domain.startup.usecase.SignInUseCaseImpl
import com.team23.domain.startup.usecase.SignUpUseCase
import com.team23.domain.startup.usecase.SignUpUseCaseImpl
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {

    factory<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName("statemachine")) }

    // Use cases
    factoryOf(::IsSetUseCase)
    factoryOf(::ContainsAtLeastOneSetUseCase)
    factoryOf(::UpdateGameAfterSetFoundUseCase)

    single { IsDeviceRegisteredUseCaseImpl() as IsDeviceRegisteredUseCase }
    single { IsUserSignedInUseCaseImpl() as IsUserSignedInUseCase }
    single { RegisterDeviceUseCaseImpl() as RegisterDeviceUseCase }
    single { SignInUseCaseImpl() as SignInUseCase }
    single { SignUpUseCaseImpl() as SignUpUseCase }

    // Game state machine
    factoryOf(::GameStateMachine)
    singleOf(::StartupStateMachine)
}
