package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card

sealed class GameEvent {
    data object CreateSolo : GameEvent()
    data object ContinueSolo : GameEvent()
    data class CardsSelected(val selectedCards: Set<Card.Data>) : GameEvent()
}
