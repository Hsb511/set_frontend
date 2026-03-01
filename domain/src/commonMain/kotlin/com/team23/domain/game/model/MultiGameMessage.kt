package com.team23.domain.game.model

import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface MultiGameMessage {
    val timestamp: Instant

    data class Connected(override val timestamp: Instant) : MultiGameMessage
    data class LobbyData(
        override val timestamp: Instant,
        val hostUsername: String,
        val players: List<String>,
    ) : MultiGameMessage

    data class GameStart(
        override val timestamp: Instant,
        val gameId: Uuid,
        val startTime: Instant,
    ) : MultiGameMessage

    data class Error(override val timestamp: Instant, val message: String) : MultiGameMessage
    data class Default(override val timestamp: Instant) : MultiGameMessage
}
