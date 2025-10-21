package com.team23.ui.shape

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

internal val previewModifier = Modifier.size(width = 100.dp, height = 200.dp)

internal class SampleFillingTypeProvider : PreviewParameterProvider<FillingTypeUiModel> {
    override val values = FillingTypeUiModel.entries.asSequence()
}
