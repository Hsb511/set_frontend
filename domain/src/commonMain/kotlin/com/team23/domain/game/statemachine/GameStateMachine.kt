package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import com.team23.domain.startup.model.GameType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class GameStateMachine(
    private val isSetUseCase: IsSetUseCase,
    private val updateGameAfterSetFoundUseCase: UpdateGameAfterSetFoundUseCase,
    private val gameRepository: GameRepository,
) {
    private val _gameSideEffect: MutableSharedFlow<GameSideEffect> = MutableSharedFlow()
    val gameSideEffect: SharedFlow<GameSideEffect> = _gameSideEffect

    suspend fun reduce(state: GameState, event: GameEvent): GameState = when (state) {

        is GameState.EmptyDeck -> when (event) {
            is GameEvent.Init -> initializeGame(event.gameType)
            else -> state
        }

        is GameState.Playing -> when (event) {
            is GameEvent.CardsSelected -> handleCardSelection(state, event)
            else -> state
        }

        is GameState.Finished -> state
    }

    private suspend fun initializeGame(gameType: GameType): GameState {
        val game = when (gameType) {
            GameType.Solo -> gameRepository.createSoloGame()
            GameType.Multi -> TODO()
        }

        return game.getOrElse {
            _gameSideEffect.emit(GameSideEffect.CannotCreateGame)
            GameState.EmptyDeck
        }
    }

    private suspend fun handleCardSelection(
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

    private suspend fun handleCardSelectionWhenNotASet(selectedCards: Set<Card>): Set<Card> =
        if (selectedCards.size == 3) {
            _gameSideEffect.emit(GameSideEffect.InvalidSet)
            emptySet()
        } else {
            selectedCards
        }
}
