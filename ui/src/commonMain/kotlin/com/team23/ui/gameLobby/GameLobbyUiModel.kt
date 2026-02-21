package com.team23.ui.gameLobby

sealed interface GameLobbyUiModel {
    data object Loading : GameLobbyUiModel

    data class Data(
        val gameName: String,
        val isHost: Boolean,
        val isPrivate: Boolean,
        val hostUsername: String,
        val allPlayers: List<Player>,
    ) : GameLobbyUiModel {
        data class Player(
            val name: String,
            val isHost: Boolean = false,
            val isYou: Boolean = false,
        )
    }
}
