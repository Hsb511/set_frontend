package com.team23.domain.statemachine

import com.team23.domain.model.Card

sealed class GameState {
    object EmptyDeck : GameState()

    data class Playing(
        val deck: List<Card>,
        val table: List<Card>,
        val selected: Set<Card> = emptySet(),
    ) : GameState()

    object Finished : GameState()
}
