package com.team23.data.game.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
sealed interface GameWsEvent {

    @Serializable
    @SerialName("connected")
    data class Connected(
        @SerialName("player_id")
        val playerId: Uuid,
        val timestamp: Instant,
    ): GameWsEvent

    @Serializable
    @SerialName("lobby_update")
    data class LobbyUpdate(
        @SerialName("public_name")
        val publicName: String,
        @SerialName("master_username")
        val masterUsername: String,
        val participants: List<String>,
        val timestamp: Instant,
    ): GameWsEvent

    @Serializable
    @SerialName("heartbeat_ack")
    data class HeartbeatAck(
        val timestamp: Instant,
    ) : GameWsEvent

    @Serializable
    @SerialName("pong")
    data class Pong(
        val timestamp: Instant,
    ) : GameWsEvent

    @Serializable
    @SerialName("error")
    data class Error(
        val message: String,
        @SerialName("error_code")
        val errorCode: Int,
        val timestamp: Instant,
    ) : GameWsEvent

    @Serializable
    @SerialName("game_start_time")
    data class GameStartTime(
        @SerialName("game_id")
        val gameId: Uuid,
        @SerialName("start_time")
        val startTime: Instant,
        val timestamp: Instant,
    ) : GameWsEvent

    @Serializable
    @SerialName("deck_uploaded")
    data class DeckUploaded(
        @SerialName("public_name")
        val publicName: String,
        val username: String,
        val turn: Int,
        val timestamp: Instant,
    ) : GameWsEvent
}
