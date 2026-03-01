package com.team23.ui.gameLobby

import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
sealed interface GameLobbyUiModel {
    enum class Loading(val text: String) : GameLobbyUiModel {
        Default("Loading multi game lobby..."),
        Creating("Creating multi game..."),
        Joining("Joining multi game..."),
        Connecting("Connecting to game session..."),
        UpdatingData("Updating game data..."),
    }

    data class Data(
        val gameName: String = "",
        val isHost: Boolean = false,
        val isPrivate: Boolean = false,
        val hostUsername: String = "",
        val allPlayers: List<Player> = emptyList(),
        val countDown: Int? = null,
    ) : GameLobbyUiModel {
        data class Player(
            val name: String,
            val isHost: Boolean = false,
            val isMe: Boolean = false,
        )
    }
}
