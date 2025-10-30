package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card

sealed class GameState {
    data object EmptyDeck : GameState()

    data class Playing(
        val deck: List<Card>,
        val table: List<Card>,
        val selected: Set<Card> = emptySet(),
    ) : GameState()

    data class Finished(
        val cards: List<Card>,
    ) : GameState()
}
