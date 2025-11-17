package com.team23.ui

import com.team23.ui.card.CardUiMapper
import com.team23.ui.game.GameUiMapper
import com.team23.ui.game.GameViewModel
import com.team23.ui.auth.AuthViewModel
import com.team23.ui.debug.DebugViewModel
import com.team23.ui.settings.SettingsViewModel
import com.team23.ui.splash.SplashViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {

    factoryOf(::CardUiMapper)
    factoryOf(::GameUiMapper)

    single {
        SplashViewModel(
            stateMachine = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        AuthViewModel(
            stateMachine = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        DebugViewModel(
            adminRepository = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        SettingsViewModel(
            userRepository = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        GameViewModel(
            stateMachine = get(),
            gameRepository = get(),
            gameUiMapper = get(),
            cardUiMapper = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }
}