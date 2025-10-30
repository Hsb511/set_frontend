package com.team23.ui.login

import com.team23.domain.startup.statemachine.StartupEvent
import com.team23.domain.startup.statemachine.StartupState
import com.team23.domain.startup.statemachine.StartupStateMachine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginViewModel(
    private val stateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)

    fun onAction(loginAction: LoginAction) {
        viewModelScope.launch {
            when (loginAction) {
                LoginAction.SignIn ->
                    stateMachine.reduce(StartupState.UserSignInUp, StartupEvent.SignIn)

                LoginAction.SignUp ->
                    stateMachine.reduce(StartupState.UserSignInUp, StartupEvent.SignUp)
            }
        }
    }
}
