package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GameStateMachine(
    private val isSetUseCase: IsSetUseCase,
    private val updateGameAfterSetFoundUseCase: UpdateGameAfterSetFoundUseCase,
    private val gameRepository: GameRepository,
) {
    private val _gameSideEffect: MutableSharedFlow<GameSideEffect> = MutableSharedFlow()
    val gameSideEffect: SharedFlow<GameSideEffect> = _gameSideEffect

    suspend fun reduce(state: GameState, event: GameEvent): GameState = when (state) {
        is GameState.EmptyDeck -> when (event) {
            is GameEvent.StartSolo -> startSoloGame(event.forceCreation)
            else -> state
        }

        is GameState.Playing -> when (event) {
            is GameEvent.CardsSelected -> handleCardSelection(state, event)
            else -> state
        }

        is GameState.Finished -> state
    }

    private suspend fun startSoloGame(forceCreation: Boolean): GameState {
        return if (forceCreation) {
            gameRepository.createSoloGame(force = true).getOrElse { throwable ->
                _gameSideEffect.emit(GameSideEffect.CannotCreateGame(throwable))
                GameState.EmptyDeck
            }
        } else {
            gameRepository.getOngoingSoloGame().getOrElse {
                gameRepository.createSoloGame(force = false).getOrElse { throwable ->
                    _gameSideEffect.emit(GameSideEffect.CannotCreateGame(throwable))
                    GameState.EmptyDeck
                }
            }
        }
    }

    private suspend fun handleCardSelection(
        state: GameState.Playing,
        event: GameEvent.CardsSelected
    ): GameState {
        if (!isSetUseCase.invoke(event.selectedCards)) {
            val newSelectedCards = handleCardSelectionWhenNotASet(event.selectedCards)
            return state.copy(gameId = state.gameId, selected = newSelectedCards)
        }

        emitSetFound(event.selectedCards, state.table)

        return updateGameAfterSetFoundUseCase.invoke(
            game = state,
            setFound = event.selectedCards,
        ).also { gameState ->
            (gameState as? GameState.Playing)?.let { playingGame ->
                gameRepository.notifySoloGameUpdated(playingGame)
            }
        }
    }

    private suspend fun emitSetFound(
        selectedCards: Set<Card.Data>,
        table: List<Card>,
    ) {
        val selectedCardsWithIndex = selectedCards.map { card ->
            table.indexOf(card) to card
        }.toSet()
        _gameSideEffect.emit(GameSideEffect.SetFound(selectedCardsWithIndex))
    }

    private suspend fun handleCardSelectionWhenNotASet(selectedCards: Set<Card>): Set<Card.Data> =
        if (selectedCards.filterIsInstance<Card.Data>().size == 3) {
            _gameSideEffect.emit(GameSideEffect.InvalidSet)
            emptySet()
        } else {
            selectedCards.filterIsInstance<Card.Data>().toSet()
        }
}
