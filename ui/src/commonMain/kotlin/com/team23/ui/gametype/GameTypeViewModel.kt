package com.team23.ui.gametype

import androidx.compose.material3.SnackbarVisuals
import androidx.navigation.NavController
import com.team23.domain.game.statemachine.GameEvent
import com.team23.domain.game.statemachine.GameSideEffect
import com.team23.domain.game.statemachine.GameState
import com.team23.domain.game.statemachine.GameStateMachine
import com.team23.domain.startup.model.GameType
import com.team23.domain.startup.statemachine.StartupEvent
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameTypeViewModel(
    private val startupStateMachine: StartupStateMachine,
    private val gameStateMachine: GameStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)
    private lateinit var navController: NavController

    val snackbar: SharedFlow<SnackbarVisuals> = gameStateMachine.gameSideEffect
        .map(::mapToSnackbar)
        .filterNotNull()
        .shareIn(viewModelScope, SharingStarted.Lazily)

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
            val newState = startupStateMachine.reduce(StartupState.GameTypeChoice, StartupEvent.StartGameType(GameType.Solo))
            if (newState is StartupState.StartGame) {
                val gameState = gameStateMachine.reduce(GameState.EmptyDeck, GameEvent.Init(GameType.Solo))
                if (gameState is GameState.Playing) {
                    navigateToGame()
                }
            }
        }
    }

    private suspend fun navigateToGame() {
        withContext(Dispatchers.Main) {
            navController.navigate(NavigationScreen.Game.name)
        }
    }

    private fun mapToSnackbar(sideEffect: GameSideEffect): SnackbarVisuals? = when (sideEffect) {
        is GameSideEffect.InvalidSet -> SetSnackbarVisuals.InvalidSet
        is GameSideEffect.CannotCreateGame -> SetSnackbarVisuals.CannotCreateGame(sideEffect.throwable.message)
        else -> null
    }
}
