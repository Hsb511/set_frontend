package com.team23.data.game

import com.team23.data.card.CardDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface CreateGameResponse {

    @Serializable
    data class Success(
        @SerialName("game_uuid")
        val gameId: Uuid,
        @SerialName("deck_index")
        val deckIndex: Int? = null,
        val turn: Int? = null,
        @SerialName("pile_count")
        val pileCount: Int? = null,
        @SerialName("table_count")
        val tableCount: Int? = null,
        @SerialName("pit_count")
        val pitCount: Int? = null,
        @SerialName("pile_cards")
        val pileCards: List<CardDataModel>? = null,
        val table: List<CardDataModel>? = null,
    ) : CreateGameResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : CreateGameResponse
}
