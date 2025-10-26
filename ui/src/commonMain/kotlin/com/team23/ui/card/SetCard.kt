package com.team23.ui.card


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.team23.ui.shape.DiamondShape
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.shape.OvalShape
import com.team23.ui.shape.SquiggleShape
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import com.team23.ui.card.Slot.CardUiModel.Color as CardColor
import com.team23.ui.card.Slot.CardUiModel.Shape as CardShape

@Composable
fun SetCard(
    card: Slot.CardUiModel,
    modifier: Modifier = Modifier,
) {
    CardContainer(
        isPortraitMode = card.isPortraitMode,
        isSelected = card.selected,
        modifier = modifier,
    ) {
        val color = card.color.toColor()
        val fillingType = card.fillingType
        val shapeModifier = Modifier.aspectRatio(ratio = 0.5f)

        for (index in 1..card.patternAmount) {
            val portraitModifier = Modifier.portraitRotation(card.patternAmount, index)
            val containerModifier =
                Modifier
                    .aspectRatio(ratio = if (card.isPortraitMode) 1f else 0.5f)
                    .then(if (card.isPortraitMode) portraitModifier else Modifier)
            CardContent(
                shape = card.shape,
                color = color,
                fillingType = fillingType,
                containerModifier = containerModifier,
                shapeModifier = shapeModifier,
            )
        }
    }
}

private fun Modifier.portraitRotation(patternAmount: Int, index: Int) = this
    .rotate(90f)
    .graphicsLayer {
        val width = size.width
        translationX = when (patternAmount) {
            1 -> 0f
            2 -> if (index == 1) width / 4 else -width / 4
            else -> when (index) {
                1 -> 0f
                2 -> -width / 2 - 5
                else -> -width / 2
            }
        }
    }

@Composable
private fun CardContent(
    shape: CardShape,
    color: Color,
    fillingType: FillingTypeUiModel,
    containerModifier: Modifier,
    shapeModifier: Modifier,
) {
    when (shape) {
        CardShape.Diamond -> DiamondShape(
            color = color,
            fillingTypeUiModel = fillingType,
            modifier = containerModifier,
            contentModifier = shapeModifier
        )

        CardShape.Oval -> OvalShape(
            color = color,
            fillingTypeUiModel = fillingType,
            modifier = containerModifier,
            contentModifier = shapeModifier
        )

        CardShape.Squiggle -> SquiggleShape(
            color = color,
            fillingTypeUiModel = fillingType,
            modifier = containerModifier,
            contentModifier = shapeModifier
        )
    }
}

@Composable
private fun CardContainer(
    isPortraitMode: Boolean,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    content: @Composable () -> Unit,
) {
    val arrangement = Arrangement.spacedBy(LocalSpacings.current.medium)

    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color.Cyan else MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier,
    ) {
        if (isPortraitMode) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LocalSpacings.current.medium),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = arrangement,
                ) {
                    content()
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = LocalSpacings.current.medium),
            ) {
                Row(horizontalArrangement = arrangement) {
                    content()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SetCardPreview(@PreviewParameter(SampleCardProvider::class) card: Slot.CardUiModel) {
    SetTheme {
        SetCard(
            card = card,
            modifier = Modifier.size(width = 160.dp, height = 90.dp)
        )
    }
}

private class SampleCardProvider : PreviewParameterProvider<Slot.CardUiModel> {
    override val values = CardColor.entries.toTypedArray().flatMap { color ->
        FillingTypeUiModel.entries.toTypedArray().flatMap { fillingType ->
            CardShape.entries.flatMap { shape ->
                (1..3).map { patternAmount ->
                    Slot.CardUiModel(
                        patternAmount = patternAmount,
                        color = color,
                        fillingType = fillingType,
                        shape = shape,
                        selected = false,
                        isPortraitMode = false,
                    )
                }
            }
        }
    }.asSequence()
}
