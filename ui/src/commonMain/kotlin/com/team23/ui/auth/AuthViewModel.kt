package com.team23.ui.auth

import androidx.compose.material3.SnackbarVisuals
import androidx.navigation.NavController
import com.team23.domain.startup.statemachine.StartupEvent
import com.team23.domain.startup.statemachine.StartupSideEffect
import com.team23.domain.startup.statemachine.StartupState
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.snackbar.SetSnackbarVisuals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val stateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)
    private lateinit var navController: NavController

    val snackbar: SharedFlow<SnackbarVisuals> = stateMachine.startupSideEffect
        .map(::mapToSnackbar)
        .shareIn(viewModelScope, SharingStarted.Lazily)

    fun setNavController(navHostController: NavController) {
        this.navController = navHostController
    }

    fun onAction(authAction: AuthAction) {
        when (authAction) {
            is AuthAction.NavigateToSignIn -> navController.navigate(NavigationScreen.SignInWithCredentials.name)
            is AuthAction.NavigateToSignUp -> navController.navigate(NavigationScreen.SignUpWithCredentials.name)
            is AuthAction.Auth -> handleAuth(authAction)
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
        viewModelScope.launch {
            val newState = stateMachine.reduce(StartupState.UserSignInUp, startupEvent)
            if (newState is StartupState.GameTypeChoice) {
                navigateToGameTypeSelection()
            }
        }
    }

    private suspend fun navigateToGameTypeSelection() {
        withContext(Dispatchers.Main) {
            navController.navigate(NavigationScreen.GameTypeSelection.name)
        }
    }

    private fun mapToSnackbar(sideEffect: StartupSideEffect): SnackbarVisuals = when (sideEffect) {
        is StartupSideEffect.SignInError -> SetSnackbarVisuals.SignInError(sideEffect.throwable.message)
        is StartupSideEffect.SignUpError -> SetSnackbarVisuals.SignUpError(sideEffect.throwable.message)
    }
}
