package com.team23.domain.statemachine

import com.team23.domain.model.Card

sealed class GameEvent {
    object Init : GameEvent()
    data class CardsSelected(val selectedCards: Set<Card>) : GameEvent()
}
