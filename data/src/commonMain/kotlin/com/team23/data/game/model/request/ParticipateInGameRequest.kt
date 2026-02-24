package com.team23.data.game.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipateInGameRequest(
    @SerialName("public_name")
    val publicName: String,
)
