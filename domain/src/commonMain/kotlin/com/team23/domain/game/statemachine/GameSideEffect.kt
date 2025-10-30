package com.team23.domain.game.statemachine

sealed interface GameSideEffect {
    data object InvalidSet: GameSideEffect
}
