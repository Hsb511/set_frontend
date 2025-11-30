package com.team23.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team23.ui.auth.AuthCredentialsScreen
import com.team23.ui.auth.AuthType
import com.team23.ui.auth.AuthTypeScreen
import com.team23.ui.game.GameScreen
import com.team23.ui.game.GameTypeScreen
import com.team23.ui.settings.SettingsScreen
import com.team23.ui.splash.SplashScreen

@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Splash.name,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = NavigationScreen.Splash.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationScreen.AuthType.name) {
            AuthTypeScreen(navController = navController)
        }
        composable(route = NavigationScreen.SignUpWithCredentials.name) {
            AuthCredentialsScreen(
                authType = AuthType.SignUp,
                navController = navController,
            )
        }
        composable(route = NavigationScreen.SignInWithCredentials.name) {
            AuthCredentialsScreen(
                authType = AuthType.SignIn,
                navController = navController,
            )
        }
        composable(route = NavigationScreen.GameTypeSelection.name) {
            GameTypeScreen(navController = navController)
        }
        composable(route = NavigationScreen.Game.name) {
            GameScreen(navController = navController)
        }
        composable(
            route = NavigationScreen.Settings.name,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) {
            SettingsScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}