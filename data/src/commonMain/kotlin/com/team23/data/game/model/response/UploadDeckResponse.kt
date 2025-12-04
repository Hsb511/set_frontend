package com.team23.data.game.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface UploadDeckResponse {

    @Serializable
    data class Success(
        val message: String,
        @SerialName("deck_index")
        val deckIndex: Int,
        @SerialName("game_completed")
        val gameCompleted: Boolean,
    ): UploadDeckResponse

    @Serializable
    data class Failure(
        val error: String,
    ): UploadDeckResponse

    data object InvalidSessionToken: UploadDeckResponse
}
