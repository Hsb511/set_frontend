package com.team23.ui.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.team23.ui.shape.FillingTypeUiModel

data class CardUiModel(
    val patternAmount: Int,
    val color: Color,
    val fillingType: FillingTypeUiModel,
    val shape: Shape,
    val selected: Boolean = false,
    val isPortraitMode: Boolean,
) {

    enum class Color {
        Primary, Secondary, Tertiary;

        @Composable
        fun toColor() = when (this) {
            Primary -> MaterialTheme.colorScheme.primary
            Secondary -> MaterialTheme.colorScheme.secondary
            Tertiary -> MaterialTheme.colorScheme.tertiary
        }
    }

    enum class Shape {
        Diamond, Oval, Squiggle
    }
}
