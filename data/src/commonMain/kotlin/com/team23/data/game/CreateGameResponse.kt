package com.team23.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface CreateGameResponse {

    @Serializable
    data class Success(
        val message: String,
        @SerialName("game_uuid")
        val gameId: Uuid,
    ) : CreateGameResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : CreateGameResponse
}
