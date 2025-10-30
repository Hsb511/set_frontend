package com.team23.ui.card

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.theming.fuchsia
import com.team23.ui.theming.kellyGreen
import com.team23.ui.theming.officeGreen
import com.team23.ui.theming.purple
import com.team23.ui.theming.red
import com.team23.ui.theming.webOrange

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
            fun toColor() = if (isSystemInDarkTheme()) {
                when (this) {
                    Primary -> webOrange
                    Secondary -> kellyGreen
                    Tertiary -> fuchsia
                }
            } else {
                when (this) {
                    Primary -> red
                    Secondary -> officeGreen
                    Tertiary -> purple
                }
            }
        }

        enum class Shape {
            Diamond, Oval, Squiggle
        }
    }
}
