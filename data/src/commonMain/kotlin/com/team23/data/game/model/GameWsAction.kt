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
    ) {
        @Serializable
        data class Data(
            val gameId: Uuid,
            val startTime: Instant,
        )
    }


}
