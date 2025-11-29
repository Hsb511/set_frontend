package com.team23.ui.navigation

enum class NavigationScreen {
    Splash,
    AuthType,
    SignInWithCredentials,
    SignUpWithCredentials,
    GameTypeSelection,
    Game,
    Settings;

    companion object {
        fun canAccessSettings(route: String?): Boolean =
            route in listOf(GameTypeSelection, Game).map { it.name }
    }
}
