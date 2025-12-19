package com.team23.ui.navigation

sealed class NavigationScreen(val name: String) {
    data object Splash: NavigationScreen("Splash")
    data object AuthType: NavigationScreen("AuthType")
    data object SignInWithCredentials: NavigationScreen("SignInWithCredentials")
    data object SignUpWithCredentials: NavigationScreen("SignUpWithCredentials")
    data object Lobby: NavigationScreen("Lobby")
    data object Game: NavigationScreen("Game")
    data object Settings: NavigationScreen("Settings")

    companion object {
        fun canAccessSettings(route: String?): Boolean =
            route in listOf(Lobby, Game).map { it.name }
    }
}
