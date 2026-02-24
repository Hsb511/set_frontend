package com.team23.data.game.model.response

import com.team23.data.card.SetCard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface ParticipateInGameResponse {

    @Serializable
    data class Success (
        @SerialName("game_id")
        val gameId: Uuid,
        @SerialName("public_name")
        val publicName: String,
        val participants: List<String>,
        val pile: List<SetCard>? = null,
        val table: List<SetCard>? = null,
        val pit: List<List<SetCard>> = emptyList(),
        @SerialName("websocket_url")
        val websocketUrl: String
    ) : ParticipateInGameResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : ParticipateInGameResponse

    data object InvalidSessionToken: ParticipateInGameResponse
}