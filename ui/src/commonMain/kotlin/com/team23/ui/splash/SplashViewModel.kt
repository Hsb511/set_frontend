package com.team23.ui.splash

import androidx.navigation.NavController
import com.team23.domain.startup.statemachine.StartupEvent
import com.team23.domain.startup.statemachine.StartupState
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.ui.card.Slot
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.shape.FillingTypeUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

class SplashViewModel(
    private val stateMachine: StartupStateMachine,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)
    private lateinit var navController: NavController

    fun setNavController(navHostController: NavController) {
        this.navController = navHostController
    }

    init {
        viewModelScope.launch {
            var screen: NavigationScreen = NavigationScreen.Splash

            val elapsed: Duration = measureTime {
                val newState = stateMachine.reduce(StartupState.Splash, StartupEvent.Init)
                screen = when (newState) {
                    // TODO SHOULD NOT BE POSSIBLE. SHOW ERROR
                    is StartupState.Splash,
                    is StartupState.StartGame -> NavigationScreen.Splash
                    is StartupState.GameTypeChoice -> NavigationScreen.GameTypeSelection
                    // TODO HANDLE DEVICE REGISTRATION
                    is StartupState.DeviceRegistration,
                    is StartupState.UserSignInUp -> NavigationScreen.LoginType
                }
            }

            val remaining = 3.seconds - elapsed
            if (remaining.isPositive()) {
                delay(remaining)
            }
            withContext(Dispatchers.Main) {
                navController.navigate(screen.name)
            }
        }
    }

    fun getRandomCard() = Slot.CardUiModel(
        isPortraitMode = false,
        patternAmount = (1..3).random(),
        color = Slot.CardUiModel.Color.entries.random(),
        fillingType = FillingTypeUiModel.entries.random(),
        shape = Slot.CardUiModel.Shape.entries.random(),
    )
}
