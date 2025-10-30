package com.team23.ui.login

sealed interface LoginAction {
    data object SignIn: LoginAction
    data object SignUp: LoginAction
}