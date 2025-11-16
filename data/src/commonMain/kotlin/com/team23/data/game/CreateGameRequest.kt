package com.team23.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class CreateGameRequest(
    @SerialName("session_token")
    val sessionToken: Uuid
)
