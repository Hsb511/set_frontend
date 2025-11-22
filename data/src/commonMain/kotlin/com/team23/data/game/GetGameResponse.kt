package com.team23.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface GetGameResponse {

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Success(
        @SerialName("game_id")
        val gameId: Uuid,
    ): GetGameResponse

    @Serializable
    data class Failure(
        val error: String,
    ): GetGameResponse

    data object InvalidSessionToken: GetGameResponse
}
