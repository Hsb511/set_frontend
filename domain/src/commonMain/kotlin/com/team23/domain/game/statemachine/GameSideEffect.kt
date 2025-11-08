package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card

sealed interface GameSideEffect {
    data object InvalidSet: GameSideEffect
    data object CannotCreateGame: GameSideEffect
    data class SetFound(val cards: Set<Pair<Int, Card.Data>>): GameSideEffect
}
