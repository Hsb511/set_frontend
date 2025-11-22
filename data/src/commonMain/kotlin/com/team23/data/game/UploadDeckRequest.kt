package com.team23.data.game

import com.team23.data.card.SetCard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class UploadDeckRequest(
    @SerialName("game_id")
    val gameId: Uuid,
    @SerialName("upload_mode")
    val uploadMode: UploadMode,
    val turn: Int,
    val pile: List<SetCard>,
    val table: List<SetCard>,
    val pit: List<List<SetCard>>,
) {

    @Serializable
    enum class UploadMode {
        @SerialName("next")
        Next,

        @SerialName("final")
        Final,
    }
}
