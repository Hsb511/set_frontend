package com.team23.domain.startup.statemachine

import com.team23.domain.startup.model.GameType

sealed interface StartupEvent {
    data object Init: StartupEvent
    data class SignIn(
        val username: String,
        val password: String,
    ): StartupEvent
    data class SignUp(
        val username: String,
        val password: String,
        val firstname: String?,
        val lastname: String?,
    ): StartupEvent
    data class StartGameType(val gameType: GameType): StartupEvent
}
