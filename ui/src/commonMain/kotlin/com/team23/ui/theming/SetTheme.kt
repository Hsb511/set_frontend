package com.team23.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject

@Composable
fun SetTheme(
    content: @Composable () -> Unit,
) {
    val themeViewModel = koinInject<ThemeViewModel>()
    val darkTheme by themeViewModel.isDarkTheme.collectAsState()

    CompositionLocalProvider(LocalSpacings provides SetSpacings()) {
        MaterialTheme(
            colorScheme = if (darkTheme ?: isSystemInDarkTheme()) darkSetColorScheme() else lightSetColorScheme(),
            shapes = setShapes(),
            content = content,
        )
    }
}
