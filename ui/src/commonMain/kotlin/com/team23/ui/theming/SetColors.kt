package com.team23.ui.theming

import androidx.compose.ui.graphics.Color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun lightSetColorScheme() = lightColorScheme(
    background = Color.White,
    onBackground = Color.Black,
)

@Composable
fun darkSetColorScheme() = darkColorScheme(
    background = Color.Black,
    onBackground = Color.White,
)

internal val officeGreen = Color(0xFF008002)
internal val kellyGreen = Color(0xFF00B803)
internal val webOrange = Color(0xFFFFB047)
internal val red = Color(0xFFFF0101)
internal val fuchsia = Color(0xFFFF47FF)
internal val purple = Color(0xFF800080)
internal val white95 = Color(0xFFF2F2F2)
internal val orange = Color(0xFFFFAA00)
