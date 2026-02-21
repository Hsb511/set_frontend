package com.team23.data.game.model.response

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
sealed interface GetOpenGamesResponse {

    @Serializable
    data class Success(
        val games: List<String>
    ) : GetOpenGamesResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : GetOpenGamesResponse

    data object InvalidSessionToken: GetOpenGamesResponse
}
