package com.team23.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("game")
data class GameDataModel(
    val id: Uuid,
    val type: Type,
    val playerIds: List<Uuid>,
    val decks: List<DeckDataModel>,
) {

    @Serializable
    enum class Type {
        @SerialName("solo")
        Solo,

        @SerialName("multi")
        Multi,
    }
}
