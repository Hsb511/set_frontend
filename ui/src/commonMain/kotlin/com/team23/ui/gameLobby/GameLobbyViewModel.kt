package com.team23.ui.gameLobby

import com.team23.domain.game.repository.GameRepository
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GameLobbyViewModel(
    private val gameRepository: GameRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {

    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)

    private val _gameLobbyUiModel = MutableStateFlow<GameLobbyUiModel>(GameLobbyUiModel.Loading)
    val gameLobbyUiModel: StateFlow<GameLobbyUiModel> = _gameLobbyUiModel

    fun start(rawGameId: String?) {
        viewModelScope.launch {
            if (rawGameId == null) {
                createMultiGame()
            } else {
                val gameId = runCatching { Uuid.parse(rawGameId) }.getOrNull()

                if (gameId == null) {
                    NavigationManager.popBackStack()
                    SnackbarManager.showMessage(SetSnackbarVisuals.FormatErrorMultiGameId(rawGameId))
                } else {
                    _gameLobbyUiModel.value = GameLobbyUiModel.Data(
                        gameId = gameId.toString(),
                        isHost = false,
                        isPrivate = true,
                        hostUsername = "Who's that?",
                        allPlayers = listOf(
                            GameLobbyUiModel.Data.Player(name = "Who's that?", isHost = true),
                            GameLobbyUiModel.Data.Player(name = "You", isYou = true),
                            GameLobbyUiModel.Data.Player(name = "You got hacked"),
                        ),
                    )
                }
            }
        }
    }

    fun onAction(action: GameLobbyAction) {
        when (action) {
            is GameLobbyAction.CopyGameId -> TODO()
            is GameLobbyAction.ChangeVisibility -> handleChangeVisibility(action.isPrivate)
            is GameLobbyAction.StartGame -> TODO()
            is GameLobbyAction.LeaveGame -> TODO()
        }
    }

    private suspend fun createMultiGame() {
        val gameId = gameRepository.createMultiGame().onFailure { error ->
            SnackbarManager.showMessage(SetSnackbarVisuals.CannotCreateMultiGame(error.message))
        }.getOrElse { Uuid.random() }
        _gameLobbyUiModel.value = GameLobbyUiModel.Data(
            gameId = gameId.toString(),
            isHost = true,
            isPrivate = true,
            hostUsername = "You",
            allPlayers = listOf(
                GameLobbyUiModel.Data.Player(name = "You", isHost = true, isYou = true),
                GameLobbyUiModel.Data.Player(name = "Who's that?"),
                GameLobbyUiModel.Data.Player(name = "You got hacked"),
            ),
        )
    }

    private fun handleChangeVisibility(private: Boolean) {
        val currentGameLobby = _gameLobbyUiModel.value as? GameLobbyUiModel.Data ?: return
        _gameLobbyUiModel.value = currentGameLobby.copy(isPrivate = private)
    }
}
