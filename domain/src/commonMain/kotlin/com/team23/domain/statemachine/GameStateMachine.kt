package com.team23.domain.statemachine

import com.team23.domain.model.Card
import com.team23.domain.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.usecase.IsSetUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class GameStateMachine(
    private val isSetUseCase: IsSetUseCase,
    private val containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase,
    private val coroutineScope: CoroutineScope,
) {
    private val _gameSideEffect: MutableSharedFlow<GameSideEffect> = MutableSharedFlow()
    val gameSideEffect: SharedFlow<GameSideEffect> = _gameSideEffect

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
            val newSelectedCards = handleCardSelectionWhenNotASet(event.selectedCards)
            return state.copy(selected = newSelectedCards)
        }

        // TODO HANDLE CASE WHEN TABLE SIZE > 12 && NEW TABLE - 3 cards is valid. i. e. from 15 to 12
        var updatedDeck = state.deck.drop(3)
        var newCards = state.deck.take(3).toMutableList()
        val newTable = replaceCardsInTable(
            table = state.table,
            cardsToRemove = event.selectedCards,
            cardsFromDeck = newCards
        ).toMutableList()

        var tableContainsNoSet = !containsAtLeastOneSetUseCase.invoke(newTable)

        while (tableContainsNoSet && updatedDeck.isNotEmpty()) {
            newCards = updatedDeck.take(3).toMutableList()
            updatedDeck = updatedDeck.drop(3)
            newTable.addAll(newCards)
            tableContainsNoSet = !containsAtLeastOneSetUseCase.invoke(newTable)
        }

        return if (updatedDeck.isEmpty() && tableContainsNoSet) {
            GameState.Finished(cards = newTable)
        } else {
            GameState.Playing(deck = updatedDeck, table = newTable)
        }
    }

    private fun handleCardSelectionWhenNotASet(selectedCards: Set<Card>): Set<Card> = if (selectedCards.size == 3) {
        coroutineScope.launch {
            _gameSideEffect.emit(GameSideEffect.InvalidSet)
        }
        emptySet()
    } else {
        selectedCards
    }

    private fun replaceCardsInTable(
        table: List<Card>,
        cardsToRemove: Set<Card>,
        cardsFromDeck: List<Card>
    ): List<Card> {
        val newTable = table.toMutableList()
        val cardsToAdd = if (cardsFromDeck.isEmpty()) {
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
