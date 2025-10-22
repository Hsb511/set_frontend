package com.team23.ui.shape


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import kotlin.math.abs

@Composable
fun DiamondShape(
    color: Color,
    fillingTypeUiModel: FillingTypeUiModel,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        DiamondPath(color = color, drawStyle = Stroke(), modifier = contentModifier)

        when (fillingTypeUiModel) {
            FillingTypeUiModel.Outlined -> Unit
            FillingTypeUiModel.Filled -> DiamondPath(color = color, drawStyle = Fill, modifier = contentModifier)
            FillingTypeUiModel.Striped -> Canvas(modifier = contentModifier) {
                val width = size.width
                val height = size.height

                for (i in -7..7) {
                    drawLine(
                        color = color,
                        strokeWidth = width / 30f,
                        start = Offset(
                            x = abs(i * width / 17) + width / 15f,
                            y = height / 2 + i * height / 17
                        ),
                        end = Offset(
                            x = width - abs(i * width / 17) - width / 15f,
                            y = height / 2 + i * height / 17
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun DiamondPath(
    color: Color,
    drawStyle: DrawStyle,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(width / 2f, width / 15f)
            lineTo(width - width / 15f, height / 2f)
            lineTo(width / 2f, height - width / 15f)
            lineTo(width / 15f, height / 2f)
            close()
        }

        val computedDrawStyle = if (drawStyle is Stroke) Stroke(width / 15f) else drawStyle

        drawPath(
            path = path,
            color = color,
            style = computedDrawStyle,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DiamondShapePreview(@PreviewParameter(SampleFillingTypeProvider::class) fillingTypeUiModel: FillingTypeUiModel) {
    SetTheme {
        DiamondShape(
            color = Color.Green,
            fillingTypeUiModel = fillingTypeUiModel,
            contentModifier = previewModifier,
        )
    }
}
