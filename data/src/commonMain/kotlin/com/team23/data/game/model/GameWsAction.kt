package com.team23.data.game.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
sealed interface GameWsAction {

    @Serializable
    @SerialName("heartbeat")
    data object Heartbeat : GameWsAction

    @Serializable
    @SerialName("ping")
    data object Ping : GameWsAction

    @Serializable
    @SerialName("game_start")
    data class GameStart(
        val data: Data
    ): GameWsAction {

        @Serializable
        data class Data(
            @SerialName("game_id")
            val gameId: Uuid,
            @SerialName("start_time")
            val startTime: Instant,
        )
    }
}
