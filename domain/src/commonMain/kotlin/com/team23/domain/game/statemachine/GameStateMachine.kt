package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class GameStateMachine(
    private val isSetUseCase: IsSetUseCase,
    private val updateGameAfterSetFoundUseCase: UpdateGameAfterSetFoundUseCase,
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

        return updateGameAfterSetFoundUseCase.invoke(
            table = state.table,
            setFound = event.selectedCards,
            deck = state.deck,
        )
    }

    private fun handleCardSelectionWhenNotASet(selectedCards: Set<Card>): Set<Card> = if (selectedCards.size == 3) {
        coroutineScope.launch {
            _gameSideEffect.emit(GameSideEffect.InvalidSet)
        }
        emptySet()
    } else {
        selectedCards
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
