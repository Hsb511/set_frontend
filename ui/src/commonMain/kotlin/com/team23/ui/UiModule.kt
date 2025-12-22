package com.team23.ui

import com.team23.ui.auth.AuthTypeViewModel
import com.team23.ui.card.CardUiMapper
import com.team23.ui.game.GameUiMapper
import com.team23.ui.game.GameViewModel
import com.team23.ui.auth.AuthViewModel
import com.team23.ui.debug.DebugViewModel
import com.team23.ui.gameLobby.GameLobbyViewModel
import com.team23.ui.gameSelection.GameSelectionViewModel
import com.team23.ui.settings.SettingsViewModel
import com.team23.ui.splash.SplashViewModel
import com.team23.ui.theming.ThemeViewModel
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
        AuthTypeViewModel(
            startupStateMachine = get(),
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
            getAllPreferencesUseCase = get(),
            togglePreferenceUseCase = get(),
            adminRepository = get(),
            authRepository = get(),
            userRepository = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        GameSelectionViewModel(
            gameRepository = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        GameLobbyViewModel(
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        GameViewModel(
            stateMachine = get(),
            findFirstSetUseCase = get(),
            gameRepository = get(),
            userRepository = get(),
            gameUiMapper = get(),
            cardUiMapper = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    single {
        ThemeViewModel(
            userRepository = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }
}