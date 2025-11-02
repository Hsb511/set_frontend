package com.team23.ui.login

sealed interface LoginAction {
    data object NavigateToSignIn: LoginAction
    data object NavigateToSignUp: LoginAction
    data class SignIn(val userId: String): LoginAction
}