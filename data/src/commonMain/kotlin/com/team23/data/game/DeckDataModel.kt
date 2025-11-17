package com.team23.data.game

import com.team23.data.card.SetCard
import kotlinx.serialization.Serializable

@Serializable
data class DeckDataModel(
    val pile: List<SetCard>,
    val table: List<SetCard>,
    val pit: List<SetCard>,
)
