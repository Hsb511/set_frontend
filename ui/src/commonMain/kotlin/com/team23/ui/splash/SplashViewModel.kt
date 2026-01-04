package com.team23.ui.splash

import com.team23.domain.startup.statemachine.StartupEvent
import com.team23.domain.startup.statemachine.StartupState
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.ui.card.Slot
import com.team23.ui.debug.showSplashScreen
import com.team23.ui.navigation.NavigationManager
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

    init {
        viewModelScope.launch {
            var screen: NavigationScreen = NavigationScreen.Splash

            val elapsed: Duration = measureTime {
                val newState = stateMachine.reduce(StartupState.Splash, StartupEvent.Init)
                screen = when (newState) {
                    is StartupState.Splash,
                    is StartupState.StartGame -> NavigationScreen.Splash
                    is StartupState.GameSelection -> NavigationScreen.GameSelection
                    is StartupState.UserSignInUp -> NavigationScreen.AuthType
                }
            }

            val total = if (showSplashScreen()) 3.seconds else 0.seconds
            val remaining = total - elapsed
            if (remaining.isPositive()) {
                delay(remaining)
            }
            withContext(Dispatchers.Main) {
                NavigationManager.handle(screen)
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
