package com.team23.ui.lobby

import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LobbyViewModel(
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val job = SupervisorJob()
    private val viewModelScope = CoroutineScope(job + dispatcher + coroutineName)

    fun onAction(action: LobbyAction) {
        when (action) {
            is LobbyAction.StartSolo -> startSoloGame()
            is LobbyAction.StartMulti -> TODO()
        }
    }


    private fun startSoloGame() {
        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.Game)
        }
    }
}