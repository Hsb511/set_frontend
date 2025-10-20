package com.team23.ui.shape.utils

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.team23.ui.shape.model.FillingType
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

internal val previewModifier = Modifier.size(width = 100.dp, height = 200.dp)

internal class SampleFillingTypeProvider : PreviewParameterProvider<FillingType> {
    override val values = FillingType.entries.asSequence()
}

internal class SampleDrawStyleProvider : PreviewParameterProvider<DrawStyle> {
    override val values: Sequence<DrawStyle> = sequenceOf(Fill, Stroke())
}
