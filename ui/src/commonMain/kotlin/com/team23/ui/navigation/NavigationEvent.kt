package com.team23.ui.navigation

sealed interface NavigationEvent {

    data class Navigate(val route: String): NavigationEvent
    data object PopBackStack: NavigationEvent
    data class ClearBackStackOrNavigate(val route: String): NavigationEvent
}