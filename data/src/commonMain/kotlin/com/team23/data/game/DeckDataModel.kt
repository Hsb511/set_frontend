package com.team23.data.game

import com.team23.data.card.CardDataModel
import kotlinx.serialization.Serializable

@Serializable
data class DeckDataModel(
    val pile: List<CardDataModel>,
    val table: List<CardDataModel>,
    val pit: List<CardDataModel>,
)
