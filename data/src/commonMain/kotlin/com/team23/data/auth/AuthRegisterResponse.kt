package com.team23.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
sealed interface AuthRegisterResponse {

    @Serializable
    data class Success(
        val message: String,
        @SerialName("player_id")
        val playerId: Uuid,
    ): AuthRegisterResponse

    @Serializable
    data class Failure(
        val error: String,
    ): AuthRegisterResponse
}
