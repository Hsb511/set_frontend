package com.team23.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team23.ui.game.GameScreen
import com.team23.ui.gametype.GameTypeScreen
import com.team23.ui.login.LoginScreen
import com.team23.ui.splash.SplashScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Splash.name
    ) {
        composable(route = NavigationScreen.Splash.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationScreen.Login.name)  {
            LoginScreen(navController = navController)
        }
        composable(route = NavigationScreen.GameTypeSelection.name) {
            GameTypeScreen(navController = navController)
        }
        composable(route = NavigationScreen.Game.name) {
            GameScreen(navController = navController)
        }
    }
}