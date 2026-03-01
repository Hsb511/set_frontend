package com.team23.domain.game.model

sealed interface MultiGameMessage {

    data object Connected: MultiGameMessage
    data class LobbyData(
        val hostUsername: String,
        val players: List<String>,
    ): MultiGameMessage

    data class Error(val message: String): MultiGameMessage
    data object Default: MultiGameMessage
}
