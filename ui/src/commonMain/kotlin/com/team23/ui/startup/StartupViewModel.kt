package com.team23.ui.startup

import com.team23.domain.startup.statemachine.StartupStateMachine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope

class StartupViewModel(
    private val stateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)


}
