package com.team23.ui.login

import androidx.navigation.NavController
import com.team23.domain.startup.statemachine.StartupEvent
import com.team23.domain.startup.statemachine.StartupState
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.ui.navigation.NavigationScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val stateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)
    private lateinit var navController: NavController

    fun setNavController(navHostController: NavController) {
        this.navController = navHostController
    }

    fun onAction(loginAction: LoginAction) {
        when (loginAction) {
            LoginAction.SignIn -> handleSignIn()
            LoginAction.SignUp -> handleSignUp()

        }
    }

    private fun handleSignIn() {
        viewModelScope.launch {
            val newState = stateMachine.reduce(StartupState.UserSignInUp, StartupEvent.SignIn)
            if (newState is StartupState.DeviceRegistration) {
                navigateToGameTypeSelection()
            }
        }
    }

    private fun handleSignUp() {
        viewModelScope.launch {
            val newState = stateMachine.reduce(StartupState.UserSignInUp, StartupEvent.SignUp)
            if (newState is StartupState.DeviceRegistration) {
                navigateToGameTypeSelection()
            }
        }
    }

    private suspend fun navigateToGameTypeSelection() {
        withContext(Dispatchers.Main) {
            navController.navigate(NavigationScreen.GameTypeSelection.name)
        }
    }
}
