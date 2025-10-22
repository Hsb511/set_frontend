package com.team23.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun SetTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalSpacings provides SetSpacings()) {
        MaterialTheme(
            colorScheme = if (darkTheme ?: isSystemInDarkTheme()) darkSetColorScheme() else lightSetColorScheme(),
            shapes = setShapes(),
            content = content,
        )
    }
}
