package com.team23.ui.auth

sealed interface AuthAction {
    data class Auth(
        val type: AuthType,
        val username: String,
        val password: String,
        val firstname: String? = null,
        val lastname: String? = null,
    ): AuthAction
}