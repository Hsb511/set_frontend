package com.team23.data.game.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGameRequest(

    @SerialName("game_mode")
    val gameMode: GameMode,
    val privacy: Privacy,
    @SerialName("response_mode")
    val responseMode: ResponseMode,
    val force: Boolean,
) {
    @Serializable
    enum class GameMode {
        @SerialName("solo")
        Solo,

        @SerialName("multi-parallel")
        MultiParallel,

        @SerialName("multi-synchronous")
        MultiSynchronous,
    }

    @Serializable
    enum class Privacy {
        @SerialName("public")
        Public,

        @SerialName("private")
        Private,
    }


    @Serializable
    enum class ResponseMode {
        @SerialName("id")
        Id,

        @SerialName("full")
        Full,
    }
}
