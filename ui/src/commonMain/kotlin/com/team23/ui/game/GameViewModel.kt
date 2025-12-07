package com.team23.ui.game

import androidx.compose.material3.SnackbarVisuals
import com.team23.domain.game.model.Card
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.statemachine.GameEvent
import com.team23.domain.game.statemachine.GameSideEffect
import com.team23.domain.game.statemachine.GameState
import com.team23.domain.game.statemachine.GameStateMachine
import com.team23.domain.game.usecase.FindFirstSetUseCase
import com.team23.domain.settings.Preference
import com.team23.domain.startup.model.GameType
import com.team23.domain.startup.repository.UserRepository
import com.team23.ui.card.CardUiMapper
import com.team23.ui.card.Slot
import com.team23.ui.game.GameAction.Restart
import com.team23.ui.game.GameAction.SelectOrUnselectCard
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val stateMachine: GameStateMachine,
    private val findFirstSetUseCase: FindFirstSetUseCase,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val gameUiMapper: GameUiMapper,
    private val cardUiMapper: CardUiMapper,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val job = SupervisorJob()
    private val viewModelScope = CoroutineScope(job + dispatcher + coroutineName)

    private val isPortraitFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val hasAnimationFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _gameStateFlow: MutableStateFlow<GameState> = MutableStateFlow(GameState.EmptyDeck)
    val gameUiModelFlow: StateFlow<GameUiModel> = combine(
        _gameStateFlow.onEach(::confirmFinishedGame),
        isPortraitFlow,
        hasAnimationFlow
    ) { game, isPortrait, hasAnimation ->
        gameUiMapper.toUiModel(game, isPortrait, hasAnimation)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = GameUiModel()
    )

    private val _gameUiEvent: MutableSharedFlow<GameUiEvent> = MutableSharedFlow()
    val gameUiEvent: SharedFlow<GameUiEvent> = merge(
        _gameUiEvent,
        stateMachine.gameSideEffect.map(::mapToEvent).filterNotNull()
    ).shareIn(viewModelScope, SharingStarted.Lazily)

    init {
        stateMachine.gameSideEffect
            .map(::mapToSnackbar)
            .filterNotNull()
            .onEach(SnackbarManager::showMessage)
            .launchIn(viewModelScope)
    }

    fun start() {
        viewModelScope.launch {
            isPortraitFlow.value = runCatching {
                userRepository.getUserPreference(Preference.CardPortrait)
            }.getOrNull() ?: false
        }
        viewModelScope.launch {
            hasAnimationFlow.value = runCatching {
                userRepository.getUserPreference(Preference.DisableAnimation)?.not()
            }.getOrNull() ?: true
        }
    }

    fun onAction(action: GameAction) {
        when (action) {
            is SelectOrUnselectCard -> selectOrUnselectCard(action.card)
            is Restart -> startNewGame()
            is GameAction.ChangeGameType -> navigate(NavigationScreen.GameTypeSelection)
            is GameAction.StartSolo -> startSoloGame()
            is GameAction.StartMulti -> TODO()
            is GameAction.RetryConfirmation -> confirmFinishedGame(_gameStateFlow.value)
            is GameAction.SelectSet -> selectSet()
        }
    }

    private fun startNewGame() {
        viewModelScope.launch {
            initSoloGame()
            _gameUiEvent.emit(GameUiEvent.ResetScreen)
        }
    }

    private fun startSoloGame() {
        viewModelScope.launch {
            initSoloGame()
            if (_gameStateFlow.value is GameState.Playing) {
                navigate(NavigationScreen.Game)
            }
        }
    }

    private suspend fun initSoloGame() {
        updateGameState(GameState.EmptyDeck, GameEvent.Init(GameType.Solo))
    }

    private fun navigate(screen: NavigationScreen) {
        viewModelScope.launch {
            NavigationManager.handle(screen)
        }
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
        viewModelScope.launch {
            updateGameState(_gameStateFlow.value, event)
        }
    }

    private suspend fun updateGameState(state: GameState, event: GameEvent) {
        _gameStateFlow.value = stateMachine.reduce(state, event)
    }

    private fun confirmFinishedGame(gameState: GameState) {
        if (gameState !is GameState.Finished) return

        viewModelScope.launch {
            gameRepository.notifySoloGameFinished(gameState)
                .onSuccess { completed ->
                    if (completed) {
                        SnackbarManager.showMessage(SetSnackbarVisuals.GameCompletionSuccess)
                    } else {
                        println("This should never happen")
                    }
                    sendCompletionEvent(completed)
                }
                .onFailure { throwable ->
                    SnackbarManager.showMessage(SetSnackbarVisuals.GameCompletionError(throwable.message))
                    sendCompletionEvent(false)
                }
        }
    }

    private suspend fun sendCompletionEvent(isConfirmed: Boolean) {
        val type = if (isConfirmed) GameCompletionType.Restart else GameCompletionType.Retry
        _gameUiEvent.emit(GameUiEvent.GameCompletion(type))
    }

    private fun mapToEvent(sideEffect: GameSideEffect): GameUiEvent? = when (sideEffect) {
        is GameSideEffect.SetFound -> GameUiEvent.AnimateSelectedCards(
            sideEffect.cards.map { (index, card) ->
                index to cardUiMapper.toUiModel(card, isSelected = false, isPortrait = isPortraitFlow.value)
            }.toSet()
        )

        else -> null
    }

    private fun mapToSnackbar(sideEffect: GameSideEffect): SnackbarVisuals? = when (sideEffect) {
        is GameSideEffect.InvalidSet -> SetSnackbarVisuals.InvalidSet
        is GameSideEffect.CannotCreateGame -> SetSnackbarVisuals.CannotCreateGame(sideEffect.throwable.message)
        else -> null
    }

    private fun selectSet() {
        val currentGame = _gameStateFlow.value
        if (currentGame !is GameState.Playing) return
        val set = findFirstSetUseCase.invoke(currentGame.table) ?: return

        viewModelScope.launch {
            updateGameState(_gameStateFlow.value, GameEvent.CardsSelected(set))
        }
    }
}
