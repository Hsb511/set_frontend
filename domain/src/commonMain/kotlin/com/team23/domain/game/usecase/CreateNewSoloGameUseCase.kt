package com.team23.domain.game.usecase

import com.team23.domain.game.statemachine.GameState

class CreateNewSoloGameUseCase(
    private val containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase,
    private val createFullShuffledDeckUseCase: CreateFullShuffledDeckUseCase,
) {

    fun invoke(): GameState.Playing {
        val fullDeck = createFullShuffledDeckUseCase.invoke()
        val table = fullDeck.take(12).toMutableList()
        var remainingDeck = fullDeck.drop(12)
        while (!containsAtLeastOneSetUseCase.invoke(table)) {
            table.addAll(remainingDeck.take(3))
            remainingDeck = remainingDeck.drop(3)
        }
        return GameState.Playing(deck = remainingDeck, table = table)
    }
}
