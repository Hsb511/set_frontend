package com.team23.domain.startup.statemachine

import com.team23.domain.startup.model.GameType

sealed interface StartupState {
    data object Splash: StartupState
    data object UserSignInUp: StartupState
    data object GameTypeChoice: StartupState
    data class StartGame(val gameType: GameType): StartupState
}
