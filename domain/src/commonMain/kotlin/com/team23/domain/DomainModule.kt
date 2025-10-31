package com.team23.domain

import com.team23.domain.game.statemachine.GameStateMachine
import com.team23.domain.game.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import com.team23.domain.startup.statemachine.StartupStateMachine
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {

    // Use cases
    factoryOf(::IsSetUseCase)
    factoryOf(::ContainsAtLeastOneSetUseCase)
    factoryOf(::UpdateGameAfterSetFoundUseCase)

    // Game state machine
    factoryOf(::GameStateMachine)
    singleOf(::StartupStateMachine)
}
