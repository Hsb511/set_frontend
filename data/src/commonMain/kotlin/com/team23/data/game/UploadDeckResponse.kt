package com.team23.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface UploadDeckResponse {

    @Serializable
    data class Success(
        val message: String,
        @SerialName("game_id")
        val gameId: Uuid,
        @SerialName("deck_index")
        val deckIndex: Int,
        @SerialName("game_completed")
        val gameCompleted: Boolean,
    ): UploadDeckResponse

    @Serializable
    data class Failure(
        val error: String,
    ): UploadDeckResponse

}
