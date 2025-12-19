package com.team23.ui.navigation

sealed interface NavigationEvent {

    data class Navigate(val navigationScreen: NavigationScreen): NavigationEvent
    data object PopBackStack: NavigationEvent
    data class ClearBackStackOrNavigate(val navigationScreen: NavigationScreen): NavigationEvent
}