package com.team23.domain.statemachine

sealed interface GameSideEffect {
    data object InvalidSet: GameSideEffect
}
