package com.team23.domain.startup.statemachine

import com.team23.domain.startup.repository.DeviceRepository
import com.team23.domain.startup.repository.UserRepository
import com.team23.domain.startup.usecase.SignInUseCase
import com.team23.domain.startup.usecase.SignUpUseCase
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class StartupStateMachine(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository,
) {

    @OptIn(ExperimentalUuidApi::class)
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
            is StartupEvent.RegisterDevice -> handleDeviceRegistration(state, event.userId)
            else -> state
        }

        is StartupState.GameTypeChoice -> when (event) {
            is StartupEvent.StartGameType -> StartupState.StartGame(event.gameType)
            else -> state
        }

        is StartupState.StartGame -> state
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun handleInitWorkflow(): StartupState {
        val isUserSignedIn = userRepository.getUserId().isSuccess
        val isDeviceRegistered = deviceRepository.getDeviceId().isSuccess
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

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun handleDeviceRegistration(state: StartupState, userId: Uuid): StartupState =
        deviceRepository.createDeviceIdAndStoreIt(userId)
            .map { StartupState.GameTypeChoice }
            .getOrElse {
                // TODO SHOW ERROR MESSAGE
                state
            }
}
