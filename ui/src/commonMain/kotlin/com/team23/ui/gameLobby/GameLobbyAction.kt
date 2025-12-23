package com.team23.ui.gameLobby

sealed interface GameLobbyAction {
    data object CopyGameId: GameLobbyAction
    data class ChangeVisibility(val isPrivate: Boolean): GameLobbyAction
    data object StartGame: GameLobbyAction
    data object LeaveGame: GameLobbyAction
}
