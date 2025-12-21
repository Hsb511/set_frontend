package com.team23.ui.auth

import androidx.compose.material3.SnackbarVisuals
import com.team23.domain.startup.statemachine.StartupEvent
import com.team23.domain.startup.statemachine.StartupSideEffect
import com.team23.domain.startup.statemachine.StartupState
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.navigation.NavigationScreen.AuthCredentials.AuthType
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthViewModel(
    private val stateMachine: StartupStateMachine,
    private val dispatcher: CoroutineDispatcher,
    private val coroutineName: CoroutineName,
) {
    private var job: CompletableJob? = null
    private var viewModelScope: CoroutineScope? = null

    fun start() {
        if (job?.isActive == true) return

        val newJob = SupervisorJob()
        job = newJob
        viewModelScope = CoroutineScope(newJob + dispatcher + coroutineName)
        observeSideEffects()
    }

    fun stop() {
        job?.cancel()
        job = null
        viewModelScope = null
    }

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.Auth -> handleAuth(action)
        }
    }

    private fun observeSideEffects() {
        viewModelScope?.launch {
            stateMachine.startupSideEffect
                .map(::mapToSnackbar)
                .filterNotNull()
                .collect(SnackbarManager::showMessage)
        }
    }

    private fun handleAuth(action: AuthAction.Auth) {
        val startupEvent = when (action.type) {
            AuthType.SignUp -> StartupEvent.SignUp(
                username = action.username,
                password = action.password,
                firstname = action.firstname,
                lastname = action.lastname,
            )
            AuthType.SignIn -> StartupEvent.SignIn(
                username = action.username,
                password = action.password,
            )
        }
        viewModelScope?.launch {
            val newState = stateMachine.reduce(StartupState.UserSignInUp, startupEvent)
            if (newState is StartupState.GameSelection) {
                NavigationManager.handle(NavigationScreen.GameSelection)
            }
        }
    }

    private fun mapToSnackbar(sideEffect: StartupSideEffect): SnackbarVisuals = when (sideEffect) {
        is StartupSideEffect.SignInError -> SetSnackbarVisuals.SignInError(sideEffect.throwable.message)
        is StartupSideEffect.SignUpError -> SetSnackbarVisuals.SignUpError(sideEffect.throwable.message)
    }
}
