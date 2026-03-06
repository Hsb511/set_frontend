package com.team23.ui.card

import androidx.compose.runtime.Composable
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.theming.officeGreen
import com.team23.ui.theming.purple
import com.team23.ui.theming.red
import kotlinx.serialization.Serializable

@Serializable
sealed interface Slot {
    val id: Int
    val isPortraitMode: Boolean

    @Serializable
    data class HoleUiModel(
        override val id: Int,
        override val isPortraitMode: Boolean,
    ) : Slot

    @Serializable
    data class CardUiModel(
        override val isPortraitMode: Boolean,
        val patternAmount: Int,
        val color: Color,
        val fillingType: FillingTypeUiModel,
        val shape: Shape,
        val selected: Boolean = false,
    ) : Slot {

        override val id: Int = patternAmount +
            10 * color.ordinal +
            100 * fillingType.ordinal +
            1000 * shape.ordinal

        @Serializable
        enum class Color {
            Primary, Secondary, Tertiary;

            @Composable
            fun toColor() = when (this) {
                Primary -> red
                Secondary -> officeGreen
                Tertiary -> purple
            }
        }

        @Serializable
        enum class Shape {
            Diamond, Oval, Squiggle
        }
    }
}
