package com.team23.ui.shape.view


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.team23.ui.shape.model.FillingType
import com.team23.ui.shape.utils.SampleFillingTypeProvider
import com.team23.ui.shape.utils.previewModifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import kotlin.math.abs

@Composable
fun OvalShape(
    color: Color,
    fillingType: FillingType,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        OvalPath(color = color, drawStyle = Stroke(), modifier = contentModifier)
        when (fillingType) {
            FillingType.Outlined -> Unit
            FillingType.Filled -> OvalPath(color = color, drawStyle = Fill, modifier = contentModifier)
            FillingType.Striped -> Canvas(modifier = contentModifier) {
                val width = size.width
                val height = size.height

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
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

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
fun OvalShapePreview(@PreviewParameter(SampleFillingTypeProvider::class) fillingType: FillingType) {
    MaterialTheme {
        OvalShape(
            color = Color.Blue,
            fillingType = fillingType,
            contentModifier = previewModifier,
        )
    }
}
