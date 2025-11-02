package com.team23.domain.startup.statemachine

import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.startup.repository.DeviceRepository
import com.team23.domain.startup.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class StartupStateMachine(
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository,
) {
    private val _startupSideEffect: MutableSharedFlow<StartupSideEffect> = MutableSharedFlow()
    val startupSideEffect: SharedFlow<StartupSideEffect> = _startupSideEffect

    @OptIn(ExperimentalUuidApi::class)
    suspend fun reduce(state: StartupState, event: StartupEvent): StartupState = when (state) {
        is StartupState.Splash -> when (event) {
            is StartupEvent.Init -> handleInitWorkflow()
            else -> state
        }

        is StartupState.UserSignInUp -> when (event) {
            is StartupEvent.SignIn -> handleSignIn(state, event.userId)
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

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun handleInitWorkflow(): StartupState {
        val isUserSignedIn = userRepository.getUserId().isSuccess
        val isDeviceRegistered = deviceRepository.getDeviceId().isSuccess
        return when {
            isUserSignedIn && isDeviceRegistered -> StartupState.GameTypeChoice
            isUserSignedIn -> StartupState.DeviceRegistration
            else -> StartupState.UserSignInUp
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun handleSignIn(state: StartupState, userId: Uuid): StartupState =
        authRepository.loginAndStoreUserId(userId)
            .map { StartupState.DeviceRegistration }
            .getOrElse { throwable ->
                _startupSideEffect.emit(StartupSideEffect.SignInError(throwable))
                println("Startup - error while signing in: ${throwable.stackTraceToString()}")
                state
            }

    private suspend fun handleSignUp(state: StartupState): StartupState =
        authRepository.registerAndStoreUserId()
            .map { StartupState.DeviceRegistration }
            .getOrElse { throwable ->
                _startupSideEffect.emit(StartupSideEffect.SignUpError(throwable))
                println("Startup - error while signing up: ${throwable.stackTraceToString()}")
                state
            }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun handleDeviceRegistration(state: StartupState): StartupState =
        deviceRepository.createDeviceIdAndStoreIt()
            .map { StartupState.GameTypeChoice }
            .getOrElse { throwable ->
                _startupSideEffect.emit(StartupSideEffect.DeviceRegistrationError(throwable))
                println("Startup - error while registering your device: ${throwable.stackTraceToString()}")
                state
            }
}
