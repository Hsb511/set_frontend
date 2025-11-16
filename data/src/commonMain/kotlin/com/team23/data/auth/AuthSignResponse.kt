package com.team23.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface AuthSignResponse {

    @Serializable
    data class Success(
        val message: String,
        @SerialName("player_id")
        val playerId: Uuid,
        @SerialName("session_token")
        val sessionToken: String,
        val username: String,
    ) : AuthSignResponse

    @Serializable
    data class Failure(
        val error: String,
    ) : AuthSignResponse
}
