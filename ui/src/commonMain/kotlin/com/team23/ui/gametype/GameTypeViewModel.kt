package com.team23.ui.gametype

import androidx.navigation.NavController
import com.team23.domain.startup.model.GameType
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

class GameTypeViewModel(
    private val stateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)
    private lateinit var navController: NavController

    fun setNavController(navHostController: NavController) {
        this.navController = navHostController
    }

    fun onAction(gameTypeAction: GameTypeAction) {
        when (gameTypeAction) {
            is GameTypeAction.StartSolo -> startSoloGame()
            is GameTypeAction.StartMulti -> TODO()
        }
    }

    private fun startSoloGame() {
        viewModelScope.launch {
            val newState = stateMachine.reduce(StartupState.GameTypeChoice, StartupEvent.StartGameType(GameType.Solo))
            if (newState is StartupState.StartGame) {
                navigateToGame()
            }
        }
    }

    private suspend fun navigateToGame() {
        withContext(Dispatchers.Main) {
            navController.navigate(NavigationScreen.Game.name)
        }
    }
}
