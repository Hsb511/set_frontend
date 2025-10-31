package com.team23.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team23.ui.game.GameScreen
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SetTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = NavigationScreen.Game.name
        ) {
            composable(route = NavigationScreen.Splash.name) {

            }
            composable(route = NavigationScreen.SignInUp.name)  {

            }
            composable(route = NavigationScreen.GameTypeSelection.name) {

            }
            composable(route = NavigationScreen.Game.name) {
                GameScreen()
            }
        }
    }
}
