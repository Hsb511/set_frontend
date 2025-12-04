package com.team23.data.game.model.request

import com.team23.data.card.SetCard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadDeckRequest(
    @SerialName("upload_mode")
    val uploadMode: UploadMode,
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