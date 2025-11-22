package com.team23.data.game.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGameRequest(

    @SerialName("game_mode")
    val gameMode: GameMode,
    @SerialName("response_mode")
    val responseMode: ResponseMode,
) {
    @Serializable
    enum class GameMode {
        @SerialName("solo")
        Solo,

        @SerialName("multi")
        Multi,
    }


    @Serializable
    enum class ResponseMode {
        @SerialName("id")
        Id,

        @SerialName("full")
        Full,
    }
}
