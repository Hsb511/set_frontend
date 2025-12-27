package com.team23.ui.gameLobby

sealed interface GameLobbyUiEvent {
    data class CopyToClipboard(val text: String) : GameLobbyUiEvent
}
