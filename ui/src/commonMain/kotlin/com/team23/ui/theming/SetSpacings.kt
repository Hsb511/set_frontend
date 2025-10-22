package com.team23.ui.theming

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class SetSpacings(
    val none: Dp = 0.dp,
    val tiny: Dp = 2.dp,
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val extraMedium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 24.dp,
    val largeIncreased: Dp = 32.dp,
    val extraLargeIncreased: Dp = 48.dp,
    val jumbo: Dp = 64.dp,
)

val LocalSpacings = compositionLocalOf { SetSpacings() }
