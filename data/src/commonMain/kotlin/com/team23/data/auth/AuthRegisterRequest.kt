package com.team23.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRegisterRequest(
    val username: String,
    val password: String,
    val name: String = "",
    val surname: String = "",
)
