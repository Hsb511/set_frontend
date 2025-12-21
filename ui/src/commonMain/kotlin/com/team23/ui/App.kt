package com.team23.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.team23.ui.debug.DebugManagementFAB
import com.team23.ui.debug.isDebug
import com.team23.ui.navigation.NavigationHost
import com.team23.ui.settings.SettingsFAB
import com.team23.ui.snackbar.SetSnackbar
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import com.team23.ui.theming.ThemeViewModel
import com.team23.ui.system.rememberSystemBarsController
import org.koin.compose.koinInject

@Composable
fun App() {
    val themeViewModel = koinInject<ThemeViewModel>()
    val useDarkScheme = themeViewModel.isDarkTheme.collectAsState().value ?: isSystemInDarkTheme()
    val systemBars = rememberSystemBarsController()

    SetTheme(useDarkScheme) {

        val background = MaterialTheme.colorScheme.background
        LaunchedEffect(useDarkScheme) {
            systemBars.setSystemBarsColor(
                statusBarColor = background,
                navigationBarColor = background,
                darkIcons = !useDarkScheme,
            )
        }
        Scaffold(
            snackbarHost = {
                SetSnackbar()
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .safeDrawingPadding()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
            ) {
                val navController = rememberNavController()

                NavigationHost(navController = navController)

                if (isDebug()) {
                    DebugManagementFAB(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(all = LocalSpacings.current.largeIncreased),
                    )
                }

                SettingsFAB(
                    navController = navController,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(all = LocalSpacings.current.largeIncreased),
                )
            }
        }
    }
}
