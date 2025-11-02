package com.team23.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ActionButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        enabled = enabled,
        contentPadding = PaddingValues(
            horizontal = LocalSpacings.current.extraLargeIncreased,
            vertical = LocalSpacings.current.largeIncreased
        ),
        modifier = modifier,
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
