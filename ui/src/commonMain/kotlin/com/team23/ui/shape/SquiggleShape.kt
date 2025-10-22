package com.team23.ui.shape


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun SquiggleShape(
    color: Color,
    fillingTypeUiModel: FillingTypeUiModel,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        SquigglePath(color = color, drawStyle = Stroke(), contentModifier)
        when (fillingTypeUiModel) {
            FillingTypeUiModel.Outlined -> Unit
            FillingTypeUiModel.Filled -> SquigglePath(color = color, drawStyle = Fill, contentModifier)
            FillingTypeUiModel.Striped -> Canvas(modifier = contentModifier) {
                val width = size.width

                drawStrokeLine(color, size, -7, 0.15f * width, 0.72f * width)
                drawStrokeLine(color, size, -6, 0.05f * width, 0.90f * width)
                drawStrokeLine(color, size, -5, 0.15f * width, 0.95f * width)
                drawStrokeLine(color, size, -4, 0.32f * width, 0.95f * width)
                drawStrokeLine(color, size, -3, 0.37f * width, 0.95f * width)
                drawStrokeLine(color, size, -2, 0.35f * width, 0.90f * width)
                drawStrokeLine(color, size, -1, 0.32f * width, 0.85f * width)
                drawStrokeLine(color, size, 0, 0.25f * width, 0.75f * width)
                drawStrokeLine(color, size, 1, 0.15f * width, 0.68f * width)
                drawStrokeLine(color, size, 2, 0.10f * width, 0.65f * width)
                drawStrokeLine(color, size, 3, 0.05f * width, 0.63f * width)
                drawStrokeLine(color, size, 4, 0.05f * width, 0.68f * width)
                drawStrokeLine(color, size, 5, 0.05f * width, 0.85f * width)
                drawStrokeLine(color, size, 6, 0.10f * width, 0.95f * width)
                drawStrokeLine(color, size, 7, 0.28f * width, 0.85f * width)
            }
        }
    }
}

fun DrawScope.drawStrokeLine(color: Color, size: Size, i: Int, x1: Float, x2: Float) {
    val width = size.width
    val height = size.height
    drawLine(
        color = color,
        strokeWidth = width / 30f,
        start = Offset(x = x1, y = height / 2 + i * height / 17),
        end = Offset(x = x2, y = height / 2 + i * height / 17),
    )
}

@Composable
fun SquigglePath(
    color: Color,
    drawStyle: DrawStyle,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(x = 2 * width / 15f, y = height / 5f)
            quadraticTo(
                x1 = width / 30f, y1 = height / 6f,
                x2 = width / 15f, y2 = height / 8f
            )
            cubicTo(
                x1 = width / 4f, y1 = -width / 15f,
                x2 = width, y2 = width / 15f,
                x3 = width - width / 23f, y3 = 2 * height / 7f,
            )
            cubicTo(
                x1 = width - width / 23f, y1 = height / 2f,
                x2 = width / 4f, y2 = 2f * height / 3f,
                x3 = width - 2* width / 15f, y3 = height - height / 5f,
            )
            quadraticTo(
                x1 = width - width / 30f, y1 = 5 * height / 6f,
                x2 = width - width / 15f, y2 = 7 * height / 8f
            )
            cubicTo(
                x1 = width - width / 4f, y1 = height + width / 15f,
                x2 = 0f, y2 = height - width / 15f,
                x3 = width / 23f, y3 = 5 * height / 7f,
            )
            cubicTo(
                x1 = width / 23f, y1 = height / 2f,
                x2 = 3 * width / 4f, y2 = height / 3f,
                x3 = 2 * width / 15f, y3 = height / 5f,
            )
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
fun SquiggleShapePreview(@PreviewParameter(SampleFillingTypeProvider::class) fillingTypeUiModel: FillingTypeUiModel) {
    SetTheme {
        SquiggleShape(
            color = Color.Red,
            fillingTypeUiModel = fillingTypeUiModel,
            contentModifier = previewModifier,
        )
    }
}
