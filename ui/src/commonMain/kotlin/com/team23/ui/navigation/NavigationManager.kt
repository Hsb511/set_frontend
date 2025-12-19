package com.team23.ui.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object NavigationManager {

    private val _navigationEvent: MutableSharedFlow<NavigationEvent> = MutableSharedFlow()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent

    suspend fun handle(screen: NavigationScreen) {
        _navigationEvent.emit(NavigationEvent.Navigate(screen))
    }

    suspend fun popBackStack() {
        _navigationEvent.emit(NavigationEvent.PopBackStack)
    }

    suspend fun clearBackStackOrNavigate(screen: NavigationScreen) {
        _navigationEvent.emit(NavigationEvent.ClearBackStackOrNavigate(screen))
    }
}
