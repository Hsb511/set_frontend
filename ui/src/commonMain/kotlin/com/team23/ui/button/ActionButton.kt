package com.team23.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun ActionButton(
    uiModel: ActionButtonUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        enabled = uiModel.enabled,
        contentPadding = PaddingValues(
            horizontal = uiModel.horizontalContentPadding(),
            vertical = uiModel.verticalContentPadding(),
        ),
        modifier = modifier,
    ) {
        Text(
            text = uiModel.text,
            style = uiModel.textStyle(),
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
@Preview
private fun ActionButtonPreview(
    @PreviewParameter(ActionButtonPreviewProvider::class) uiModel: ActionButtonUiModel,
) {
    SetTheme {
        ActionButton(
            uiModel = uiModel,
            onClick = {},
        )
    }
}

private class ActionButtonPreviewProvider: PreviewParameterProvider<ActionButtonUiModel> {
    override val values: Sequence<ActionButtonUiModel> = ActionButtonUiModel.Size.entries.flatMap { size ->
        listOf(true, false).map { enabled ->
            ActionButtonUiModel(
                text =  "Action button",
                size = size,
                enabled = enabled
            )
        }
    }.asSequence()
}
