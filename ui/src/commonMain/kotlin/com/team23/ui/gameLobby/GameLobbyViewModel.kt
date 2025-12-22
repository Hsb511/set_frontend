package com.team23.ui.gameLobby

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
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {

    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)

    private val _gameLobbyUiModel = MutableStateFlow<GameLobbyUiModel>(GameLobbyUiModel.Loading)
    val gameLobbyUiModel: StateFlow<GameLobbyUiModel> = _gameLobbyUiModel

    fun start(gameId: Uuid?) {
        viewModelScope.launch {
            // Create game
            if (gameId == null) {
                // TODO CREATE
                /*_gameLobbyUiModel.value = GameLobbyUiModel.Data(
                    gameId = gameId.toString(),
                    isHost = true,

                    )*/
            // Join game
            } else {
                /*_gameLobbyUiModel.value = GameLobbyUiModel.Data(
                    gameId = gameId.toString(),
                    isHost = false,

                )*/
            }
        }
    }
}
