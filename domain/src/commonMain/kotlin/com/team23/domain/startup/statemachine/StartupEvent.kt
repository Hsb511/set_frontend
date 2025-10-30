package com.team23.domain.startup.statemachine

import com.team23.domain.startup.model.GameType

sealed interface StartupEvent {
    data object Init: StartupEvent
    data object SignIn: StartupEvent
    data object SignUp: StartupEvent
    data object RegisterDevice: StartupEvent
    data class StartGameType(val gameType: GameType): StartupEvent
}
