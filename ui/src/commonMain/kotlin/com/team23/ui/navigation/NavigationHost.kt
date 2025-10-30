package com.team23.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team23.ui.game.GameScreen
import com.team23.ui.login.LoginScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Login.name
    ) {
        composable(route = NavigationScreen.Splash.name) {

        }
        composable(route = NavigationScreen.Login.name)  {
            LoginScreen()
        }
        composable(route = NavigationScreen.GameTypeSelection.name) {

        }
        composable(route = NavigationScreen.Game.name) {
            GameScreen()
        }
    }
}