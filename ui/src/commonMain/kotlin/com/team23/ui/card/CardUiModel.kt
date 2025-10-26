package com.team23.ui.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.team23.ui.shape.FillingTypeUiModel

sealed interface CardUiModel {
    val isPortraitMode: Boolean

    data class Empty(override val isPortraitMode: Boolean) : CardUiModel

    data class Data(
        override val isPortraitMode: Boolean,
        val patternAmount: Int,
        val color: Color,
        val fillingType: FillingTypeUiModel,
        val shape: Shape,
        val selected: Boolean = false,
    ) : CardUiModel

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
