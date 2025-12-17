package com.team23.ui.navigation

enum class NavigationScreen {
    Splash,
    AuthType,
    SignInWithCredentials,
    SignUpWithCredentials,
    Lobby,
    Game,
    Settings;

    companion object {
        fun canAccessSettings(route: String?): Boolean =
            route in listOf(Lobby, Game).map { it.name }
    }
}
