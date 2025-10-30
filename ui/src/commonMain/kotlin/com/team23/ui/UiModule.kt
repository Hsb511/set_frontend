package com.team23.ui

import com.team23.ui.card.CardUiMapper
import com.team23.ui.game.GameUiMapper
import com.team23.ui.game.GameViewModel
import com.team23.ui.gametype.GameTypeViewModel
import com.team23.ui.login.LoginViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {

    factoryOf(::CardUiMapper)
    factoryOf(::GameUiMapper)

    factory {
        LoginViewModel(
            stateMachine = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    factory {
        GameTypeViewModel(
            stateMachine = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }

    factory {
        GameViewModel(
            stateMachine = get(),
            gameUiMapper = get(),
            cardUiMapper = get(),
            dispatcher = Dispatchers.Default,
            coroutineName = CoroutineName("viewmodel"),
        )
    }
}