package com.team23.ui.gameLobby

sealed interface GameLobbyAction {
    data class CopyGameId(val rawGameId: String): GameLobbyAction
    data class ChangeVisibility(val isPrivate: Boolean): GameLobbyAction
    data object StartGame: GameLobbyAction
    data object LeaveGame: GameLobbyAction
}
