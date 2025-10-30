package com.team23.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        contentPadding = PaddingValues(
            horizontal = LocalSpacings.current.extraLargeIncreased,
            vertical = LocalSpacings.current.largeIncreased
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
@Preview
private fun ActionButtonPreview() {
    SetTheme {
        ActionButton(
            text = "Action button",
            onClick = {},
        )
    }
}
