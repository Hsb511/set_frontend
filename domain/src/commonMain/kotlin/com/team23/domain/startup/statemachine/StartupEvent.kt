package com.team23.domain.startup.statemachine

import com.team23.domain.startup.model.GameType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface StartupEvent {
    data object Init: StartupEvent
    data object SignIn: StartupEvent
    data object SignUp: StartupEvent
    @OptIn(ExperimentalUuidApi::class)
    data class RegisterDevice(val userId: Uuid): StartupEvent
    data class StartGameType(val gameType: GameType): StartupEvent
}
