package com.team23.domain.game.usecase

import com.team23.domain.game.statemachine.GameState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CreateNewSoloGameUseCase(
    private val containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase,
    private val createFullShuffledDeckUseCase: CreateFullShuffledDeckUseCase,
) {

    @OptIn(ExperimentalUuidApi::class)
    fun invoke(): GameState.Playing {
        val fullDeck = createFullShuffledDeckUseCase.invoke()
        val table = fullDeck.take(12).toMutableList()
        var remainingDeck = fullDeck.drop(12)
        while (!containsAtLeastOneSetUseCase.invoke(table)) {
            table.addAll(remainingDeck.take(3))
            remainingDeck = remainingDeck.drop(3)
        }
        return GameState.Playing(gameId = Uuid.random(), deck = remainingDeck, table = table)
    }
}
