package com.team23.domain.startup.statemachine

import com.team23.domain.startup.model.GameType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface StartupEvent {
    data object Init: StartupEvent
    @OptIn(ExperimentalUuidApi::class)
    data class SignIn(val userId: Uuid): StartupEvent
    data object SignUp: StartupEvent
    data object RegisterDevice: StartupEvent
    data class StartGameType(val gameType: GameType): StartupEvent
}
