package com.team23.ui.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.team23.ui.shape.FillingTypeUiModel

sealed interface Slot {
    val isPortraitMode: Boolean

    data class HoleUiModel(override val isPortraitMode: Boolean) : Slot

    data class CardUiModel(
        override val isPortraitMode: Boolean,
        val patternAmount: Int,
        val color: Color,
        val fillingType: FillingTypeUiModel,
        val shape: Shape,
        val selected: Boolean = false,
    ) : Slot {
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
}
