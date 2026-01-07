package com.team23.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AdaptativeContainer(
    horizontal: Boolean,
    modifier: Modifier = Modifier,
    arrangement: Arrangement.HorizontalOrVertical,
    content: @Composable () -> Unit,
) {
    if (horizontal) {
        Row(
            horizontalArrangement = arrangement,
            modifier = modifier,
        ) {
            content()
        }
    } else {
        Column(
            verticalArrangement = arrangement,
            modifier = modifier,
        ) {
            content()
        }
    }
}
