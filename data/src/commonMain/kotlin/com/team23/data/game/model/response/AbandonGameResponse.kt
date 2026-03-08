package com.team23.data.game.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface AbandonGameResponse {

    @Serializable
    data class Success(
        @SerialName("game_id")
        val gameId: Uuid,
        @SerialName("game_mode")
        val gameMode: String,
        val message: String,
    ) : AbandonGameResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : AbandonGameResponse

    data object InvalidSessionToken: AbandonGameResponse
}
