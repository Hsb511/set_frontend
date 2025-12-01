package com.team23.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.koin.compose.koinInject

@Composable
fun SetTheme(
    content: @Composable () -> Unit,
) {
    val themeViewModel = koinInject<ThemeViewModel>()
    val useDarkScheme = themeViewModel.isDarkTheme.collectAsState().value ?: isSystemInDarkTheme()
    val colorScheme = if (useDarkScheme) darkSetColorScheme() else lightSetColorScheme()
    val systemBars = rememberSystemBarsController()

    LaunchedEffect(useDarkScheme) {
        systemBars.setSystemBarsColor(
            statusBarColor = colorScheme.background,
            navigationBarColor = colorScheme.background,
            darkIcons = !useDarkScheme,
        )
    }

    CompositionLocalProvider(LocalSpacings provides SetSpacings()) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = setShapes(),
            content = content,
        )
    }
}
