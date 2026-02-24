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
import kotlinx.coroutines.flow.update
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
        val multiGames = mapToMultiGames(publicMultiGames)
        _gameSelectionUiModel.update { currentUiModel ->
            when (currentUiModel) {
                is GameSelectionUiModel.Data -> currentUiModel.copy(
                    hasAnOngoingSoloGame = hasActiveSoloGame,
                    multiGames = multiGames,
                )
                is GameSelectionUiModel.Loading -> GameSelectionUiModel.Data(
                    hasAnOngoingSoloGame = hasActiveSoloGame,
                    multiGames = multiGames
                )
            }
        }
    }

    private fun mapToMultiGames(publicNameGames: List<String>): List<GameSelectionUiModel.Data.MultiGame> = publicNameGames.map { publicName ->
        GameSelectionUiModel.Data.MultiGame(
            publicName = publicName,
            playersCount = 0,
            gameMode = MultiGameMode.TimeTrial,
        )
    }

    fun onAction(action: GameSelectionAction) {
        when (action) {
            is GameSelectionAction.StartSolo -> startSoloGame(action.forceCreate)
            is GameSelectionAction.CreateVersus -> createMultiGame(MultiGameMode.Versus)
            is GameSelectionAction.CreateTimeTrial -> createMultiGame(MultiGameMode.TimeTrial)
            is GameSelectionAction.JoinMulti -> joinMultiGame(action.publicName)
        }
    }

    private fun startSoloGame(forceCreate: Boolean) {
        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.Game(forceCreate))
        }
    }

    private fun createMultiGame(multiGameMode: MultiGameMode) {
        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.GameLobby(gameName = null, multiGameMode = multiGameMode))
        }
    }

    private fun joinMultiGame(gameName: String) {
        viewModelScope.launch {
            // TODO HANDLE THAT
            NavigationManager.handle(NavigationScreen.GameLobby(gameName = gameName, multiGameMode = MultiGameMode.TimeTrial))
        }
    }
}