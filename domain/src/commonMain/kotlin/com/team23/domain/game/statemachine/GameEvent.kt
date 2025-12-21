package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card

sealed class GameEvent {
    data class CreateSolo(val force: Boolean) : GameEvent()
    data object ContinueSolo : GameEvent()
    data class CardsSelected(val selectedCards: Set<Card.Data>) : GameEvent()
}
