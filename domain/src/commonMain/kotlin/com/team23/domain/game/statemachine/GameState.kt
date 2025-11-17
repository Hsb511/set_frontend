package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class GameState {

    data object EmptyDeck : GameState()

    data class Playing(
        val gameId: Uuid,
        val deck: List<Card>,
        val table: List<Card>,
        val selected: Set<Card.Data> = emptySet(),
        val setsFound: List<Set<Card.Data>> = emptyList(),
    ) : GameState()

    data class Finished(
        val gameId: Uuid,
        val cards: List<Card>,
        val setsFound: List<Set<Card.Data>> = emptyList(),
    ) : GameState()
}
