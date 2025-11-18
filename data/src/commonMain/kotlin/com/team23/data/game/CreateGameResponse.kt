package com.team23.data.game

import com.team23.data.card.SetCard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface CreateGameResponse {

    @Serializable
    data class Success(
        @SerialName("game_id")
        val gameId: Uuid,
        @SerialName("pile_cards")
        val pileCards: List<SetCard>? = null,
        val table: List<SetCard>? = null,
    ) : CreateGameResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : CreateGameResponse
}
