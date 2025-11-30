package com.team23.domain

import com.team23.domain.game.statemachine.GameStateMachine
import com.team23.domain.game.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.game.usecase.CreateFullShuffledDeckUseCase
import com.team23.domain.game.usecase.CreateFullShuffledDeckUseCaseImpl
import com.team23.domain.game.usecase.CreateNewSoloGameUseCase
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import com.team23.domain.settings.GetAllPreferencesUseCase
import com.team23.domain.settings.TogglePreferenceUseCase
import com.team23.domain.startup.statemachine.StartupStateMachine
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {

    // Use cases
    factoryOf(::ContainsAtLeastOneSetUseCase)
    single<CreateFullShuffledDeckUseCase> { CreateFullShuffledDeckUseCaseImpl() }
    factoryOf(::CreateNewSoloGameUseCase)
    factoryOf(::GetAllPreferencesUseCase)
    factoryOf(::IsSetUseCase)
    factoryOf(::TogglePreferenceUseCase)
    factoryOf(::UpdateGameAfterSetFoundUseCase)

    // Game state machine
    singleOf(::GameStateMachine)
    singleOf(::StartupStateMachine)
}
