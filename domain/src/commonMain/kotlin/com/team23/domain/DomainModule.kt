package com.team23.domain

import com.team23.domain.game.statemachine.GameStateMachine
import com.team23.domain.game.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factory<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName("statemachine")) }

    // Use cases
    factoryOf(::IsSetUseCase)
    factoryOf(::ContainsAtLeastOneSetUseCase)
    factoryOf(::UpdateGameAfterSetFoundUseCase)

    // Game state machine
    factoryOf(::GameStateMachine)
}
