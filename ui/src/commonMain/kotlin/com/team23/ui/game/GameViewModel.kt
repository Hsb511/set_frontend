package com.team23.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team23.domain.statemachine.GameEvent
import com.team23.domain.statemachine.GameState
import com.team23.domain.statemachine.GameStateMachine
import com.team23.ui.card.CardUiMapper
import com.team23.ui.card.CardUiModel
import com.team23.ui.game.GameAction.SelectOrUnselectCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GameViewModel(
    private val stateMachine: GameStateMachine,
    private val gameUiMapper: GameUiMapper,
    private val cardUiMapper: CardUiMapper,
) : ViewModel() {

    private var isPortrait: Boolean = true
    private val _gameStateFlow: MutableStateFlow<GameState> = MutableStateFlow(GameState.EmptyDeck)
    val gameUiModelFlow: StateFlow<GameUiModel> = _gameStateFlow
        .map { game -> gameUiMapper.toUiModel(game, isPortrait) }
        .stateIn(viewModelScope, SharingStarted.Lazily, GameUiModel())

    init {
        _gameStateFlow.value = stateMachine.reduce(_gameStateFlow.value, GameEvent.Init)
    }

    fun onAction(action: GameAction) {
        when (action) {
            is SelectOrUnselectCard -> selectOrUnselectCard(action.card)
        }
    }

    private fun selectOrUnselectCard(card: CardUiModel) {
        val currentGame = _gameStateFlow.value
        if (currentGame !is GameState.Playing) return

        val cardToToggle = cardUiMapper.toDomainModel(card)
        val selectedCards = currentGame.selected.toMutableSet()
        if (cardToToggle in selectedCards) {
            selectedCards.remove(cardToToggle)
        } else {
            selectedCards.add(cardToToggle)
        }
        val event = GameEvent.CardsSelected(selectedCards)
        _gameStateFlow.value = stateMachine.reduce(_gameStateFlow.value, event)
    }
}
