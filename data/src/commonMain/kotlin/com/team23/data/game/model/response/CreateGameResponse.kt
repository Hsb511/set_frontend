package com.team23.data.game.model.response

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
        @SerialName("public_name")
        val publicName: String,
        @SerialName("date_started")
        val dateStarted: String,
        @SerialName("master_player_id")
        val masterPlayerId: Uuid,
        val status: String,
        val pile: List<SetCard>? = null,
        val table: List<SetCard>? = null,
    ) : CreateGameResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : CreateGameResponse

    data object InvalidSessionToken: CreateGameResponse
}
