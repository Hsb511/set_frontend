package com.team23.domain.game.model

import com.team23.domain.game.statemachine.GameState

data class GameLobby(
    val publicName: String,
    val game: GameState.Playing,
    val gameMode: GameMode,
    val players: List<Player>,
) {
    data class Player(
        val name: String,
        val isMe: Boolean,
        val isHost: Boolean,
    )
}
