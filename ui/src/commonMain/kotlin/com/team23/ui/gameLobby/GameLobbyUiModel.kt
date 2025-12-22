package com.team23.ui.gameLobby

data class GameLobbyUiModel(
    val gameId: String,
    val isHost: Boolean,
    val isPrivate: Boolean,
    val hostUsername: String,
    val allPlayers: List<Player>,
) {
    data class Player(
        val name: String,
        val isHost: Boolean = false,
        val isYou: Boolean = false,
    )
}
