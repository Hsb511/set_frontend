package com.team23.ui.auth

sealed interface AuthTypeAction {
    data object NavigateToSignIn: AuthTypeAction
    data object NavigateToSignUp: AuthTypeAction
}
