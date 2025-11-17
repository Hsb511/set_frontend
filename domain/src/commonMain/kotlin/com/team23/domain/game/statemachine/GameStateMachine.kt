package com.team23.domain.game.statemachine

import com.team23.domain.game.model.Card
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.usecase.CreateNewSoloGameUseCase
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import com.team23.domain.startup.model.GameType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GameStateMachine(
    private val createNewSoloGameUseCase: CreateNewSoloGameUseCase,
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
        return when (gameType) {
            GameType.Solo -> gameRepository.getOngoingSoloGame().getOrElse {
                gameRepository.createSoloGame().getOrElse { throwable ->
                    _gameSideEffect.emit(GameSideEffect.CannotCreateGame(throwable))
                    GameState.EmptyDeck
                }
            }
            GameType.Multi -> TODO()
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
        )
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
