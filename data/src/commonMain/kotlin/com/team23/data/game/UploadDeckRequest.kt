package com.team23.data.game

import com.team23.data.card.SetCard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class UploadDeckRequest(
    @SerialName("game_uuid")
    val gameId: Uuid,
    val uploadMode: UploadMode,
    val turn: Int,
    @SerialName("pile_cards")
    val pileCards: List<SetCard>,
    val table: List<String>,
    @SerialName("pit_sets")
    val pitSets: List<List<SetCard>>,
) {

    @Serializable
    enum class UploadMode {
        @SerialName("next")
        Next,

        @SerialName("final")
        Final,
    }
}
