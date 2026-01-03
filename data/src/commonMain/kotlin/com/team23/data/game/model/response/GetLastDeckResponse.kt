package com.team23.data.game.model.response

import com.team23.data.card.SetCard
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

sealed interface GetLastDeckResponse {

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Success(
        val pile: List<SetCard>? = null,
        val table: List<SetCard>? = null,
        val pit: List<List<SetCard>> = emptyList(),
    ): GetLastDeckResponse

    @Serializable
    data class Failure(
        val error: String,
    ): GetLastDeckResponse

    data object InvalidSessionToken: GetLastDeckResponse
}
