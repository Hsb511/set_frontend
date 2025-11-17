package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class GameState {
    data object EmptyDeck : GameState()

    @OptIn(ExperimentalUuidApi::class)
    data class Playing(
        val gameId: Uuid,
        val deck: List<Card>,
        val table: List<Card>,
        val selected: Set<Card.Data> = emptySet(),
    ) : GameState()

    @OptIn(ExperimentalUuidApi::class)
    data class Finished(
        val gameId: Uuid,
        val cards: List<Card>,
    ) : GameState()
}
