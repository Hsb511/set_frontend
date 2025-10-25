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
        val newState = state.copy(selected = event.selectedCards)
        if (event.selectedCards.size != 3) return newState

        val updatedDeck = newState.deck.drop(3)
        val newCards = newState.deck.take(3).toMutableList()
        val newTable = replaceCardsInTable(newState.table, newState.selected, newCards)

        val isDeckEmpty = updatedDeck.isEmpty()
        val isTableEmpty = newTable.isEmpty()

        return if (isDeckEmpty && isTableEmpty) {
            GameState.Finished
        } else {
            GameState.Playing(deck = updatedDeck, table = newTable)
        }
    }

    private fun replaceCardsInTable(
        table: List<Card>,
        cardsToRemove: Set<Card>,
        cardsToAdd: MutableList<Card>
    ): List<Card> {
        val newTable = table.toMutableList()
        cardsToRemove.onEach { cardToRemove ->
            val index = table.indexOf(cardToRemove)
            newTable[index] = cardsToAdd.removeFirst()
        }
        return newTable
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
