package com.team23.ui.auth

import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthTypeViewModel(
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private var viewModelScope = CoroutineScope(dispatcher + coroutineName)

    fun onAction(action: AuthTypeAction) {
        when (action) {
            is AuthTypeAction.NavigateToSignIn -> navigate(NavigationScreen.SignInWithCredentials)
            is AuthTypeAction.NavigateToSignUp -> navigate(NavigationScreen.SignUpWithCredentials)
        }
    }

    private fun navigate(screen: NavigationScreen) {
        viewModelScope.launch {
            NavigationManager.handle(screen)
        }
    }
}
