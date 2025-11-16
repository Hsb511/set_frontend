package com.team23.ui.auth

sealed interface AuthAction {
    data object NavigateToSignIn: AuthAction
    data object NavigateToSignUp: AuthAction
    data class SignIn(val userId: String): AuthAction
}