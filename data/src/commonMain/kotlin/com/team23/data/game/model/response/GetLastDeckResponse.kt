package com.team23.data.game.model.response

import com.team23.data.card.SetCard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface GetLastDeckResponse {

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class Success(
        val pile: List<SetCard>? = null,
        val table: List<SetCard>? = null,
    ): GetLastDeckResponse

    @Serializable
    data class Failure(
        val error: String,
    ): GetLastDeckResponse

    data object InvalidSessionToken: GetLastDeckResponse
}
