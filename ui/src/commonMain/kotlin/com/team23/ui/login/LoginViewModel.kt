package com.team23.ui.login

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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class LoginViewModel(
    private val stateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)
    private lateinit var navController: NavController

    private val _snackbar: MutableSharedFlow<SnackbarVisuals> = MutableSharedFlow()
    val snackbar: SharedFlow<SnackbarVisuals> = merge(
        _snackbar,
        stateMachine.startupSideEffect
            .map(::mapToSnackbar)
            .filterNotNull()
    ).shareIn(viewModelScope, SharingStarted.Lazily)

    fun setNavController(navHostController: NavController) {
        this.navController = navHostController
    }

    fun onAction(loginAction: LoginAction) {
        when (loginAction) {
            is LoginAction.NavigateToSignIn -> navController.navigate(NavigationScreen.LoginCredentials.name)
            is LoginAction.NavigateToSignUp -> handleSignUp()
            is LoginAction.SignIn -> handleSignIn(loginAction.userId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun handleSignIn(userId: String) {
        viewModelScope.launch {
            val signInEvent = StartupEvent.SignIn(Uuid.parse(userId))
            val newState = stateMachine.reduce(StartupState.UserSignInUp, signInEvent)
            handleDeviceRegistration(newState)
        }
    }

    private fun handleSignUp() {
        viewModelScope.launch {
            val newState = stateMachine.reduce(StartupState.UserSignInUp, StartupEvent.SignUp)
            handleDeviceRegistration(newState)
        }
    }

    private suspend fun handleDeviceRegistration(newState: StartupState) {
        if (newState is StartupState.DeviceRegistration) {
            val newState = stateMachine.reduce(StartupState.DeviceRegistration, StartupEvent.RegisterDevice)
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
        is StartupSideEffect.DeviceRegistrationError -> SetSnackbarVisuals.DeviceRegistration
        is StartupSideEffect.SignInError -> SetSnackbarVisuals.SignInError
        is StartupSideEffect.SignUpError -> SetSnackbarVisuals.SignUpError(sideEffect.throwable.message)
    }
}
