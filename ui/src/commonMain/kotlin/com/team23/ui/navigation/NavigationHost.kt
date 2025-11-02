package com.team23.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team23.ui.game.GameScreen
import com.team23.ui.gametype.GameTypeScreen
import com.team23.ui.login.LoginCredentialsScreen
import com.team23.ui.login.LoginScreen
import com.team23.ui.splash.SplashScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Splash.name,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = NavigationScreen.Splash.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationScreen.LoginType.name)  {
            LoginScreen(navController = navController)
        }
        composable(route = NavigationScreen.LoginCredentials.name) {
            LoginCredentialsScreen(navController = navController)
        }
        composable(route = NavigationScreen.GameTypeSelection.name) {
            GameTypeScreen(navController = navController)
        }
        composable(route = NavigationScreen.Game.name) {
            GameScreen(navController = navController)
        }
    }
}