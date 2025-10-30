package com.team23.domain.startup.statemachine

import com.team23.domain.startup.usecase.IsDeviceRegisteredUseCase
import com.team23.domain.startup.usecase.IsUserSignedInUseCase
import com.team23.domain.startup.usecase.RegisterDeviceUseCase
import com.team23.domain.startup.usecase.SignInUseCase
import com.team23.domain.startup.usecase.SignUpUseCase


class StartupStateMachine(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val isDeviceRegisteredUseCase: IsDeviceRegisteredUseCase,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
) {

    suspend fun reduce(state: StartupState, event: StartupEvent): StartupState = when (state) {
        is StartupState.Splash -> when (event) {
            is StartupEvent.Init -> handleInitWorkflow()
            else -> state
        }

        is StartupState.UserSignInUp -> when (event) {
            is StartupEvent.SignIn -> handleSignIn(state)
            is StartupEvent.SignUp -> handleSignUp(state)
            else -> state
        }

        is StartupState.DeviceRegistration -> when (event) {
            is StartupEvent.RegisterDevice -> handleDeviceRegistration(state)
            else -> state
        }

        is StartupState.GameTypeChoice -> when (event) {
            is StartupEvent.StartGameType -> StartupState.StartGame(event.gameType)
            else -> state
        }

        is StartupState.StartGame -> state
    }

    private suspend fun handleInitWorkflow(): StartupState {
        val isUserSignedIn = isUserSignedInUseCase.invoke()
        val isDeviceRegistered = isDeviceRegisteredUseCase.invoke()
        return when {
            isUserSignedIn && isDeviceRegistered -> StartupState.GameTypeChoice
            isUserSignedIn -> StartupState.DeviceRegistration
            else -> StartupState.UserSignInUp
        }
    }

    private suspend fun handleSignIn(state: StartupState): StartupState =
        if (signInUseCase.invoke().isSuccess) {
            StartupState.DeviceRegistration
        } else {
            // TODO SHOW ERROR MESSAGE
            state
        }

    private suspend fun handleSignUp(state: StartupState): StartupState =
        if (signUpUseCase.invoke().isSuccess) {
            StartupState.DeviceRegistration
        } else {
            // TODO SHOW ERROR MESSAGE
            state
        }

    private suspend fun handleDeviceRegistration(state: StartupState): StartupState =
        if (registerDeviceUseCase.invoke().isSuccess) {
            StartupState.GameTypeChoice
        } else {
            // TODO SHOW ERROR MESSAGE
            state
        }
}
