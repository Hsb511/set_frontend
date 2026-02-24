package com.team23.ui.gameLobby

import com.team23.domain.game.model.GameMode
import com.team23.domain.game.usecase.CreateOrJoinLobbyUseCase
import com.team23.ui.gameSelection.MultiGameMode
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GameLobbyViewModel(
    private val createOrJoinLobbyUseCase: CreateOrJoinLobbyUseCase,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {

    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)

    private val _gameLobbyUiModel = MutableStateFlow<GameLobbyUiModel>(GameLobbyUiModel.Loading)
    val gameLobbyUiModel: StateFlow<GameLobbyUiModel> = _gameLobbyUiModel

    private val _gameLobbyUiEvent = MutableSharedFlow<GameLobbyUiEvent>()
    val gameLobbyUiEvent: SharedFlow<GameLobbyUiEvent> = _gameLobbyUiEvent

    fun start(gameName: String?, multiGameMode: MultiGameMode) {
        viewModelScope.launch {
            val gameMode = when(multiGameMode) {
                MultiGameMode.TimeTrial -> GameMode.TimeTrial
                MultiGameMode.Versus -> GameMode.Versus
            }
            createOrJoinLobbyUseCase.invoke(gameName, gameMode).onSuccess { gameLobby ->
                _gameLobbyUiModel.value = GameLobbyUiModel.Data(
                    gameName = gameLobby.publicName,
                    hostUsername = gameLobby.players.first().name,
                    allPlayers = gameLobby.players.map { player ->
                        GameLobbyUiModel.Data.Player(
                            name = player.name,
                            isHost = player.isHost,
                            isMe = player.isMe,
                        )
                    },
                )
            }.onFailure { failure ->
                NavigationManager.popBackStack()
                val snackbar = if (gameName == null) {
                    SetSnackbarVisuals.CannotCreateMultiGame(failure.message)
                } else {
                    SetSnackbarVisuals.CannotJoinMultiGame(gameName, failure.message)
                }
                SnackbarManager.showMessage(snackbar)
            }
        }
    }

    fun onAction(action: GameLobbyAction) {
        when (action) {
            is GameLobbyAction.CopyGameId -> handleCopyGameId(action.rawGameId)
            is GameLobbyAction.ChangeVisibility -> handleChangeVisibility(action.isPrivate)
            is GameLobbyAction.StartGame -> handleStartGame()
            is GameLobbyAction.LeaveGame -> handleLeaveGame()
        }
    }

    private fun handleStartGame() {
        // TODO FOR MOCK PURPOSES. HANDLE PROPERLY
        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.Game(forceCreate = false))
        }
    }


    private fun handleLeaveGame() {
        // TODO NOTIFY THE PLAYER LEFT THE LOBBY
        viewModelScope.launch {
            NavigationManager.popBackStack()
        }
    }

    private fun handleCopyGameId(rawGameId: String) {
        viewModelScope.launch {
            _gameLobbyUiEvent.emit(GameLobbyUiEvent.CopyToClipboard(rawGameId))
        }
    }

    private fun handleChangeVisibility(private: Boolean) {
        val currentGameLobby = _gameLobbyUiModel.value as? GameLobbyUiModel.Data ?: return
        _gameLobbyUiModel.value = currentGameLobby.copy(isPrivate = private)
    }
}
