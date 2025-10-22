package com.team23.ui.theming

import androidx.compose.ui.graphics.Color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun lightSetColorScheme() = lightColorScheme(
    primary = red,
    secondary = officeGreen,
    tertiary = purple,
)

@Composable
fun darkSetColorScheme() = darkColorScheme(
    primary = webOrange,
    secondary = kellyGreen,
    tertiary = fuchsia,
)

internal val officeGreen = Color(0xFF008002)
internal val kellyGreen = Color(0xFF00B803)
internal val webOrange = Color(0xFFFFB047)
internal val red = Color(0xFFFF0101)
internal val fuchsia = Color(0xFFFF47FF)
internal val purple = Color(0xFF800080)
