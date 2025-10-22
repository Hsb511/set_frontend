package com.team23.ui.gameZone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team23.ui.card.CardUiModel
import com.team23.ui.card.SetCard
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun GameZone(
    gameZone: GameZoneUiModel,
    modifier: Modifier = Modifier,
) {
    val columnsCount = getGridColumnsCount(gameZone.isPortrait)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        contentPadding = PaddingValues(all = LocalSpacings.current.small),
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        items(gameZone.cards) { card ->
            SetCard(
                card = card,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .padding(all = LocalSpacings.current.small)
                    .shadow(
                        elevation = if (card.selected) 32.dp else 0.dp,
                        shape = MaterialTheme.shapes.medium,
                        spotColor = Color.Cyan,
                    )
                    .clickable { /* TODO */ }
                    .fillMaxWidth()
                    .aspectRatio(getCardAspectRation(gameZone.isPortrait)),
            )
        }
    }
}

private fun getGridColumnsCount(isPortraitMode: Boolean): Int =
    if (isPortraitMode) 4 else 3

private fun getCardAspectRation(isPortraitMode: Boolean): Float =
    if (isPortraitMode) 9 / 16f else 16 / 9f

@Composable
@Preview(showBackground = true)
private fun GameZonePreview(@PreviewParameter(PortraitPreviewProvider::class) isPortrait: Boolean) {
    SetTheme {
        GameZone(
            gameZone = GameZoneUiModel(
                cards = listOf(
                    CardUiModel(
                        patternAmount = 1,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Oval,
                        selected = true,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 2,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 3,
                        color = CardUiModel.Color.Tertiary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 1,
                        color = CardUiModel.Color.Tertiary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 2,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardUiModel.Shape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 3,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = true,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 1,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardUiModel.Shape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 2,
                        color = CardUiModel.Color.Tertiary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 3,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 1,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 2,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel(
                        patternAmount = 3,
                        color = CardUiModel.Color.Tertiary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardUiModel.Shape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                ),
                isPortrait = isPortrait,
            )
        )
    }
}

private class PortraitPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}
