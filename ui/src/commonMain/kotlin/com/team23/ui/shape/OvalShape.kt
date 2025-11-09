package com.team23.ui.shape


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import kotlin.math.abs

@Composable
fun OvalShape(
    color: Color,
    fillingTypeUiModel: FillingTypeUiModel,
    isPortrait: Boolean,
    modifier: Modifier = Modifier,
) {
    var containerWidth by remember { mutableStateOf(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(ratio = if (isPortrait) 2f else 0.5f)
            .onSizeChanged { size ->
                containerWidth = size.width.toFloat()
            },
    ) {
        val contentModifier = Modifier
            .fillMaxSize()
            .rotate(if (isPortrait) 90f else 0f)
            .graphicsLayer {
                translationX = if (isPortrait) containerWidth / 4f else 0f
                translationY = if (isPortrait) -containerWidth / 4f else 0f
            }

        // Border
        OvalPath(
            color = color,
            drawStyle = Stroke(),
            isPortrait = isPortrait,
            modifier = contentModifier,
        )

        // Filling
        when (fillingTypeUiModel) {
            FillingTypeUiModel.Outlined -> Unit
            FillingTypeUiModel.Filled -> OvalPath(
                color = color,
                drawStyle = Fill,
                isPortrait = isPortrait,
                modifier = contentModifier,
            )

            FillingTypeUiModel.Striped -> Canvas(modifier = contentModifier) {
                val width = if (isPortrait) size.height else size.width
                val height = if (isPortrait) size.width else size.height


                for (i in -7..7) {
                    when {
                        abs(i) <= 4 -> drawLine(
                            color = color,
                            strokeWidth = width / 30f,
                            start = Offset(
                                x = width / 30f,
                                y = height / 2 + i * height / 17
                            ),
                            end = Offset(
                                x = width - width / 30f,
                                y = height / 2 + i * height / 17
                            ),
                        )

                        abs(i) <= 6 -> drawLine(
                            color = color,
                            strokeWidth = width / 30f,
                            start = Offset(
                                x = width / 15f,
                                y = height / 2 + i * height / 17
                            ),
                            end = Offset(
                                x = width - width / 15f,
                                y = height / 2 + i * height / 17
                            ),
                        )

                        abs(i) == 7 -> drawLine(
                            color = color,
                            strokeWidth = width / 30f,
                            start = Offset(
                                x = width / 6f,
                                y = height / 2 + i * height / 17
                            ),
                            end = Offset(
                                x = width - width / 6f,
                                y = height / 2 + i * height / 17
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun OvalPath(
    color: Color,
    drawStyle: DrawStyle,
    isPortrait: Boolean,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = if (isPortrait) size.height else size.width
        val height = if (isPortrait) size.width else size.height

        val path = Path().apply {
            moveTo(x = width / 30f, y = width / 2f)
            quadraticTo(
                x1 = width / 15f, y1 = width / 15f,
                x2 = width / 2f, y2 = width / 15f
            )
            quadraticTo(
                x1 = width - width / 15f, y1 = width / 15f,
                x2 = width - width / 30f, y2 = width / 2f
            )
            lineTo(x = width - width / 30f, y = 3 * height / 4f)
            quadraticTo(
                x1 = width - width / 15f, y1 = height - width / 15f,
                x2 = width / 2f, y2 = height - width / 15f
            )
            quadraticTo(
                x1 = width / 15f, y1 = height - width / 15f,
                x2 = width / 30f, y2 = height - width / 2f
            )
            lineTo(x = width / 30f, y = height / 4f)
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
private fun OvalShapePreview(
    @PreviewParameter(SampleFillingTypeProvider::class) values: Pair<FillingTypeUiModel, Boolean>,
) {
    SetTheme {
        OvalShape(
            color = Color.Blue,
            fillingTypeUiModel = values.first,
            isPortrait = values.second,
            modifier = previewModifier(values.second),
        )
    }
}
