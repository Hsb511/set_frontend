package com.team23.ui.auth

import com.team23.ui.navigation.NavigationScreen.AuthCredentials.AuthType

sealed interface AuthAction {
    data class Auth(
        val type: AuthType,
        val username: String,
        val password: String,
        val firstname: String? = null,
        val lastname: String? = null,
    ): AuthAction
}