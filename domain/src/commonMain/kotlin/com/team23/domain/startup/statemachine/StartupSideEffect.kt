package com.team23.domain.startup.statemachine

sealed interface StartupSideEffect {
    data class SignInError(val throwable: Throwable): StartupSideEffect
    data class SignUpError(val throwable: Throwable): StartupSideEffect

}