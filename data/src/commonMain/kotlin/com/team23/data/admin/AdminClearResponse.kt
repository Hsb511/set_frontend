package com.team23.data.admin

import kotlinx.serialization.Serializable

sealed interface AdminClearResponse {

    @Serializable
    data class Success(
        val message: String,
    ): AdminClearResponse

    @Serializable
    data class Failure(
        val error: String,
    ): AdminClearResponse
}