package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card
import com.team23.domain.startup.model.GameType

sealed class GameEvent {
    data class Init(val gameType: GameType) : GameEvent()
    data class CardsSelected(val selectedCards: Set<Card>) : GameEvent()
}
