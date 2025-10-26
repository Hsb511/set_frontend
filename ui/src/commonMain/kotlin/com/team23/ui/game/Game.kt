package com.team23.ui.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
fun Game(
    game: GameUiModel,
    modifier: Modifier = Modifier,
    onAction: (GameAction) -> Unit = {},
) {
    val columnsCount = getGridColumnsCount(game.isPortrait)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        contentPadding = PaddingValues(all = LocalSpacings.current.small),
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        items(game.playingCards) { card ->
            SetCard(
                card = card,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .padding(all = LocalSpacings.current.small)
                    .shadow(
                        elevation = if (card is CardUiModel.Data && card.selected) 32.dp else 0.dp,
                        shape = MaterialTheme.shapes.medium,
                        spotColor = Color.Cyan,
                    )
                    .clickable { onAction(GameAction.SelectOrUnselectCard(card)) }
                    .fillMaxWidth()
                    .aspectRatio(getCardAspectRation(game.isPortrait)),
            )
        }
        item(span = { GridItemSpan(columnsCount) }) {
            Text(
                text = "${game.cardsInDeck.size} cards remaining in deck",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = LocalSpacings.current.small)
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
        Game(
            game = GameUiModel(
                cardsInDeck = emptyList(),
                playingCards = listOf(
                    CardUiModel.Data(
                        patternAmount = 1,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Oval,
                        selected = true,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 2,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 3,
                        color = CardUiModel.Color.Tertiary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 1,
                        color = CardUiModel.Color.Tertiary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 2,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardUiModel.Shape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 3,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = true,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 1,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardUiModel.Shape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 2,
                        color = CardUiModel.Color.Tertiary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 3,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 1,
                        color = CardUiModel.Color.Primary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardUiModel.Shape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
                        patternAmount = 2,
                        color = CardUiModel.Color.Secondary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardUiModel.Shape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    CardUiModel.Data(
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
