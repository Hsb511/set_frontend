package com.team23.ui.gameSelection

import com.team23.domain.game.repository.GameRepository
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GameSelectionViewModel(
    private val gameRepository: GameRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val job = SupervisorJob()
    private val viewModelScope = CoroutineScope(job + dispatcher + coroutineName)

    private val _gameSelectionUiModel: MutableStateFlow<GameSelectionUiModel> =
        MutableStateFlow(GameSelectionUiModel.Loading)
    val gameSelectionUiModel: StateFlow<GameSelectionUiModel> = _gameSelectionUiModel

    fun onStart() {
        viewModelScope.launch {
            val hasActiveSoloGame = gameRepository.hasActiveSoloGame().isSuccess
            updateState(hasActiveSoloGame)
            gameRepository.publicMultiGames().collect { publicMultiGames ->
                updateState(hasActiveSoloGame, publicMultiGames)
            }
        }
    }

    private fun updateState(hasActiveSoloGame: Boolean, publicMultiGames: List<String> = emptyList()) {
        _gameSelectionUiModel.value = when (val currentUiModel = _gameSelectionUiModel.value) {
            is GameSelectionUiModel.Data -> currentUiModel.copy(hasAnOngoingSoloGame = hasActiveSoloGame)
            is GameSelectionUiModel.Loading -> GameSelectionUiModel.Data(
                hasAnOngoingSoloGame = hasActiveSoloGame,
                multiGames = publicMultiGames.map { publicName ->
                    GameSelectionUiModel.Data.MultiGame(
                        publicName = publicName,
                        playersCount = 23,
                        type = GameSelectionUiModel.Data.MultiGame.Type.entries.random(),
                    )
                }
            )
        }
    }

    fun onAction(action: GameSelectionAction) {
        when (action) {
            is GameSelectionAction.StartSolo -> startSoloGame(action.forceCreate)
            is GameSelectionAction.CreateVersus -> createMultiGame()
            is GameSelectionAction.CreateTimeTrial -> createMultiGame()
            is GameSelectionAction.JoinMulti -> joinMultiGame(action.rawGameId)
        }
    }

    private fun startSoloGame(forceCreate: Boolean) {
        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.Game(forceCreate))
        }
    }

    private fun createMultiGame() {
        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.GameLobby(gameId = null))
        }
    }

    private fun joinMultiGame(rawGameId: String) {
        val gameId = runCatching { Uuid.parse(rawGameId) }.getOrNull()
        if (gameId == null) {
            viewModelScope.launch {
                SnackbarManager.showMessage(SetSnackbarVisuals.FormatErrorMultiGameId(rawGameId))
            }
            return
        }

        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.GameLobby(gameId = gameId.toString()))
        }
    }
}