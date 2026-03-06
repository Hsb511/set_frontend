package com.team23.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.team23.ui.theming.SetTheme

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
            maxLines = uiModel.maxLines,
            overflow = TextOverflow.Ellipsis,
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
