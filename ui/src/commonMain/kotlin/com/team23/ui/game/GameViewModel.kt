package com.team23.ui.game

import androidx.compose.material3.SnackbarVisuals
import androidx.navigation.NavController
import com.team23.domain.game.model.Card
import com.team23.domain.game.statemachine.GameEvent
import com.team23.domain.game.statemachine.GameSideEffect
import com.team23.domain.game.statemachine.GameState
import com.team23.domain.game.statemachine.GameStateMachine
import com.team23.domain.startup.model.GameType
import com.team23.ui.card.CardUiMapper
import com.team23.ui.card.Slot
import com.team23.ui.game.GameAction.Restart
import com.team23.ui.game.GameAction.SelectOrUnselectCard
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.snackbar.SetSnackbarVisuals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val stateMachine: GameStateMachine,
    private val gameUiMapper: GameUiMapper,
    private val cardUiMapper: CardUiMapper,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)
    private lateinit var navController: NavController

    private var isPortrait: Boolean = true
    private val _gameStateFlow: MutableStateFlow<GameState> = MutableStateFlow(GameState.EmptyDeck)
    val gameUiModelFlow: StateFlow<GameUiModel> = _gameStateFlow
        .map { game -> gameUiMapper.toUiModel(game, isPortrait) }
        .stateIn(viewModelScope, SharingStarted.Lazily, GameUiModel())

    private val _gameUiEvent: MutableSharedFlow<GameUiEvent> = MutableSharedFlow()
    val gameUiEvent: SharedFlow<GameUiEvent> = merge(
        _gameUiEvent,
        stateMachine.gameSideEffect.map(::mapToEvent).filterNotNull()
    ).shareIn(viewModelScope, SharingStarted.Lazily)

    val snackbar: SharedFlow<SnackbarVisuals> = stateMachine.gameSideEffect
        .map(::mapToSnackbar)
        .filterNotNull()
        .shareIn(viewModelScope, SharingStarted.Lazily)

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun onAction(action: GameAction) {
        when (action) {
            is SelectOrUnselectCard -> selectOrUnselectCard(action.card)
            is Restart -> {
                viewModelScope.launch {
                    _gameUiEvent.emit(GameUiEvent.ResetScreen)
                }
                startGame()
            }
            is GameAction.ChangeGameType -> navController.navigate(NavigationScreen.GameTypeSelection.name)
        }
    }

    private fun startGame() {
        updateGameState(GameState.EmptyDeck, GameEvent.Init(GameType.Solo))
    }

    private fun selectOrUnselectCard(card: Slot) {
        val currentGame = _gameStateFlow.value
        if (currentGame !is GameState.Playing) return

        val cardToToggle = cardUiMapper.toDomainModel(card) as Card.Data
        val selectedCards = currentGame.selected.toMutableSet()
        if (cardToToggle in selectedCards) {
            selectedCards.remove(cardToToggle)
        } else {
            selectedCards.add(cardToToggle)
        }
        val event = GameEvent.CardsSelected(selectedCards)
        updateGameState(_gameStateFlow.value, event)
    }

    private fun updateGameState(state: GameState, event: GameEvent) {
        viewModelScope.launch {
            _gameStateFlow.value = stateMachine.reduce(state, event)
        }
    }

    private fun mapToEvent(sideEffect: GameSideEffect): GameUiEvent? = when (sideEffect) {
        is GameSideEffect.SetFound -> GameUiEvent.AnimateSelectedCards(
            sideEffect.cards.map { (index, card) ->
                val isPortrait = isPortrait
                index to cardUiMapper.toUiModel(card, isSelected = false, isPortrait = isPortrait)
            }.toSet()
        )
        else -> null
    }

    private fun mapToSnackbar(sideEffect: GameSideEffect): SnackbarVisuals? = when (sideEffect) {
        is GameSideEffect.InvalidSet -> SetSnackbarVisuals.InvalidSet
        is GameSideEffect.CannotCreateGame -> SetSnackbarVisuals.CannotCreateGame(sideEffect.throwable.message)
        else -> null
    }
}
