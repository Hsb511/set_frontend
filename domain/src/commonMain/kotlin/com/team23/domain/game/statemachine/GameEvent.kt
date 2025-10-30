package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card

sealed class GameEvent {
    object Init : GameEvent()
    data class CardsSelected(val selectedCards: Set<Card>) : GameEvent()
}
