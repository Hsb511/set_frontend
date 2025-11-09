package com.team23.ui.shape

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

internal fun previewModifier(isPortrait: Boolean) = if (isPortrait) {
    Modifier.size(width = 200.dp, height = 100.dp)
} else {
    Modifier.size(width = 100.dp, height = 200.dp)
}

internal class SampleFillingTypeProvider : PreviewParameterProvider<Pair<FillingTypeUiModel, Boolean>> {
    override val values = FillingTypeUiModel.entries.asSequence().map { it to true } +
        FillingTypeUiModel.entries.asSequence().map { it to false }
}
