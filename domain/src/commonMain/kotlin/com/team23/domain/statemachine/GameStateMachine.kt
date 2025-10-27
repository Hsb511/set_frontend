package com.team23.domain.statemachine

import com.team23.domain.model.Card
import com.team23.domain.usecase.IsSetUseCase

class GameStateMachine(
    private val isSetUseCase: IsSetUseCase,
) {

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
        if (!isSetUseCase.invoke(event.selectedCards)) {
            val selectedCards = if (event.selectedCards.size == 3) {
                // TODO SEND SIDE EFFECT
                emptySet()
            } else {
                event.selectedCards
            }
            return state.copy(selected = selectedCards)
        }

        val newState = state.copy(selected = event.selectedCards)

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
        cardsFromDeck: List<Card>
    ): List<Card> {
        val newTable = table.toMutableList()
        val cardsToAdd = if (cardsFromDeck.isEmpty() && table.size <= 12) {
            MutableList(3) { Card.Empty }
        } else {
            cardsFromDeck.toMutableList()
        }
        cardsToRemove.onEach { cardToRemove ->
            val index = table.indexOf(cardToRemove)
            if (index < newTable.size) {
                newTable[index] = cardsToAdd.removeAt(0)
            }
        }

        return newTable
    }

    private fun createFullDeck(): List<Card> {
        val colors = Card.Data.Color.entries.toTypedArray()
        val shapes = Card.Data.Shape.entries.toTypedArray()
        val numbers = 1..3
        val fills = Card.Data.Fill.entries.toTypedArray()

        return colors.flatMap { color ->
            shapes.flatMap { shape ->
                numbers.flatMap { number ->
                    fills.map { fill ->
                        Card.Data(color, shape, number, fill)
                    }
                }
            }
        }
    }
}
