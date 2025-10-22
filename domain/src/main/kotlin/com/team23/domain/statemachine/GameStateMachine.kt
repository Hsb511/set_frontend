package com.team23.domain.statemachine

import com.team23.domain.model.Card

class GameStateMachine {

    fun reduce(state: GameState, event: GameEvent): GameState = when (state) {

        is GameState.EmptyDeck -> when (event) {
            is GameEvent.Init -> initializeGame()
            else -> state
        }

        is GameState.Playing -> when (event) {
            is GameEvent.CardsSelected -> handleCardSelection(state, event)
            else -> state
        }

        is GameState.Finished -> state
    }

    private fun initializeGame(): GameState {
        val fullDeck = createFullDeck().shuffled()
        val table = fullDeck.take(12)
        val remainingDeck = fullDeck.drop(12)
        return GameState.Playing(deck = remainingDeck, table = table)
    }

    private fun handleCardSelection(
        state: GameState.Playing,
        event: GameEvent.CardsSelected
    ): GameState {
        if (event.selectedCards.size != 3) return state

        val updatedDeck = state.deck.drop(3)
        val newCards = state.deck.take(3)
        val newTable = state.table - event.selectedCards + newCards

        val isDeckEmpty = updatedDeck.isEmpty()
        val isTableEmpty = newTable.isEmpty()

        return if (isDeckEmpty && isTableEmpty) {
            GameState.Finished
        } else {
            GameState.Playing(deck = updatedDeck, table = newTable)
        }
    }

    private fun createFullDeck(): List<Card> {
        val colors = Card.Color.entries.toTypedArray()
        val shapes = Card.Shape.entries.toTypedArray()
        val numbers = 1..3
        val fills = Card.Fill.entries.toTypedArray()

        return colors.flatMap { color ->
            shapes.flatMap { shape ->
                numbers.flatMap { number ->
                    fills.map { fill ->
                        Card(color, shape, number, fill)
                    }
                }
            }
        }
    }
}
