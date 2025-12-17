package com.team23.domain.startup.statemachine

import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.user.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class StartupStateMachine(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    private val _startupSideEffect: MutableSharedFlow<StartupSideEffect> = MutableSharedFlow()
    val startupSideEffect: SharedFlow<StartupSideEffect> = _startupSideEffect

    suspend fun reduce(state: StartupState, event: StartupEvent): StartupState = when (state) {
        is StartupState.Splash -> when (event) {
            is StartupEvent.Init -> handleInitWorkflow()
            else -> state
        }

        is StartupState.UserSignInUp -> when (event) {
            is StartupEvent.SignIn -> handleSignIn(state, event)
            is StartupEvent.SignUp -> handleSignUp(state, event)
            is StartupEvent.GuestRegistration -> handleGuestRegistration(state)
            else -> state
        }

        is StartupState.GameTypeChoice -> when (event) {
            is StartupEvent.StartGameType -> StartupState.StartGame(event.gameType)
            else -> state
        }

        is StartupState.StartGame -> state
    }

    private suspend fun handleInitWorkflow(): StartupState {
        val isUserSignedIn = userRepository.getUserInfo().isSuccess
        return when {
            isUserSignedIn -> StartupState.GameTypeChoice
            else -> StartupState.UserSignInUp
        }
    }

    private suspend fun handleSignIn(state: StartupState, event: StartupEvent.SignIn): StartupState = with (event) {
        authRepository.loginAndStoreUserInfo(username, password)
            .map { StartupState.GameTypeChoice }
            .getOrElse { throwable ->
                _startupSideEffect.emit(StartupSideEffect.SignInError(throwable))
                println("Startup - error while signing in: ${throwable.stackTraceToString()}")
                state
            }
    }

    private suspend fun handleSignUp(state: StartupState, event: StartupEvent.SignUp): StartupState = with (event) {
        authRepository.registerAndStoreUserInfo(username, password, firstname, lastname)
            .map { StartupState.GameTypeChoice }
            .getOrElse { throwable ->
                _startupSideEffect.emit(StartupSideEffect.SignUpError(throwable))
                println("Startup - error while signing up: ${throwable.stackTraceToString()}")
                state
            }
    }

    private suspend fun handleGuestRegistration(state: StartupState): StartupState {
        val username = Uuid.random().toString()
        val password = Uuid.random().toString()
        return authRepository.registerAndStoreUserInfo(username, password, isGuest = true)
            .map { StartupState.GameTypeChoice }
            .getOrElse { throwable ->
                _startupSideEffect.emit(StartupSideEffect.SignInError(throwable))
                println("Startup - error while registering as a guest: ${throwable.stackTraceToString()}")
                state
            }
    }
}
