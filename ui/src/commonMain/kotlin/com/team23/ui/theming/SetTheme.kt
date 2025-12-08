package com.team23.ui.theming

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun SetTheme(
    useDarkScheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (useDarkScheme) darkSetColorScheme() else lightSetColorScheme()

    CompositionLocalProvider(LocalSpacings provides SetSpacings()) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = setShapes(),
            content = content,
        )
    }
}
