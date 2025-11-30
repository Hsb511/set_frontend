package com.team23.data.auth

import kotlinx.serialization.Serializable

sealed interface AuthSignOutResponse {

    @Serializable
    data class Success(val message: String): AuthSignOutResponse

    @Serializable
    data class Failure(val error: String): AuthSignOutResponse
}
