package com.team23.ui.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.team23.ui.theming.LocalSpacings

data class ActionButtonUiModel(
    val text: String,
    val size: Size = Size.Large,
    val enabled: Boolean = true,
) {

    enum class Size {
        Small, Large
    }

    @Composable
    fun horizontalContentPadding(): Dp = when(size) {
        Size.Small -> LocalSpacings.current.extraLarge
        Size.Large -> LocalSpacings.current.extraLargeIncreased
    }

    @Composable
    fun verticalContentPadding(): Dp = when(size) {
        Size.Small -> LocalSpacings.current.large
        Size.Large -> LocalSpacings.current.largeIncreased
    }

    @Composable
    fun textStyle(): TextStyle = when(size) {
        Size.Small -> MaterialTheme.typography.titleMedium
        Size.Large -> MaterialTheme.typography.headlineMedium
    }
}
