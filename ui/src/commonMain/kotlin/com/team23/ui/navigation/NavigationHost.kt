package com.team23.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.team23.ui.auth.AuthCredentialsScreen
import com.team23.ui.auth.AuthTypeScreen
import com.team23.ui.game.GameScreen
import com.team23.ui.lobby.LobbyScreen
import com.team23.ui.settings.SettingsScreen
import com.team23.ui.splash.SplashScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                NavigationManager.navigationEvent.collect { event ->
                    when (event) {
                        is NavigationEvent.Navigate -> navController.navigate(event.navigationScreen)
                        is NavigationEvent.PopBackStack -> navController.popBackStack()
                        is NavigationEvent.ClearBackStackOrNavigate -> if (!navController.clearBackStack(event.navigationScreen)) {
                            navController.navigate(event.navigationScreen)
                        }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Splash,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable<NavigationScreen.Splash> {
            SplashScreen()
        }
        composable<NavigationScreen.AuthType> {
            AuthTypeScreen()
        }
        composable<NavigationScreen.AuthCredentials> { authCredentials ->
            val authType = authCredentials.toRoute<NavigationScreen.AuthCredentials>().authType
            AuthCredentialsScreen(authType = authType)
        }
        composable<NavigationScreen.Lobby> {
            LobbyScreen()
        }
        composable<NavigationScreen.Game> { game ->
            val startType = game.toRoute<NavigationScreen.Game>().startType
            GameScreen(startType = startType)
        }
        composable(
            route = NavigationScreen.Settings.name,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) {
            SettingsScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
