package com.team23.ui.auth

import com.team23.domain.startup.statemachine.StartupEvent
import com.team23.domain.startup.statemachine.StartupState
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthTypeViewModel(
    private val startupStateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private var viewModelScope = CoroutineScope(dispatcher + coroutineName)

    fun onAction(action: AuthTypeAction) {
        when (action) {
            is AuthTypeAction.NavigateToSignIn -> navigate(NavigationScreen.SignInWithCredentials)
            is AuthTypeAction.NavigateToSignUp -> navigate(NavigationScreen.SignUpWithCredentials)
            is AuthTypeAction.PlayAsGuest -> handlePlayAsGuest()
        }
    }

    private fun navigate(screen: NavigationScreen) {
        viewModelScope.launch {
            NavigationManager.handle(screen)
        }
    }

    private fun handlePlayAsGuest() {
        viewModelScope.launch {
            val newState = startupStateMachine.reduce(
                state = StartupState.UserSignInUp,
                event = StartupEvent.GuestRegistration
            )
            if (newState is StartupState.Lobby) {
                navigate(NavigationScreen.Lobby)
            } else {
                SnackbarManager.showMessage(SetSnackbarVisuals.GuestRegistrationError)
            }
        }
    }
}
