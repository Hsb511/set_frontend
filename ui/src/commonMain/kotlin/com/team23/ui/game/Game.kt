package com.team23.ui.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.team23.ui.card.SetCard
import com.team23.ui.card.Slot
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import com.team23.ui.card.Slot.CardUiModel.Color as CardColor
import com.team23.ui.card.Slot.CardUiModel.Shape as CardShape

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
        items(game.playingCards) { slot ->
            Slot(
                slot = slot,
                isPortrait = game.isPortrait,
                onAction = onAction,
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

@Composable
private fun Slot(
    slot: Slot,
    isPortrait: Boolean,
    onAction: (GameAction) -> Unit,
) {
    when (slot) {
        is Slot.CardUiModel -> SetCard(
            card = slot,
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .padding(all = LocalSpacings.current.small)
                .shadow(
                    elevation = if (slot.selected) 32.dp else 0.dp,
                    shape = MaterialTheme.shapes.medium,
                    spotColor = Color.Cyan,
                )
                .clickable { onAction(GameAction.SelectOrUnselectCard(slot)) }
                .fillMaxWidth()
                .aspectRatio(getCardAspectRation(isPortrait)),
        )

        is Slot.HoleUiModel -> Box(
            Modifier
                .padding(all = LocalSpacings.current.small)
                .fillMaxWidth()
                .aspectRatio(getCardAspectRation(isPortrait))
        )
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
                    Slot.CardUiModel(
                        patternAmount = 1,
                        color = CardColor.Primary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardShape.Oval,
                        selected = true,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 2,
                        color = CardColor.Secondary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardShape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 3,
                        color = CardColor.Tertiary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardShape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 1,
                        color = CardColor.Tertiary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardShape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 2,
                        color = CardColor.Primary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardShape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 3,
                        color = CardColor.Secondary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardShape.Squiggle,
                        selected = true,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 1,
                        color = CardColor.Secondary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardShape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 2,
                        color = CardColor.Tertiary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardShape.Diamond,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 3,
                        color = CardColor.Primary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardShape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 1,
                        color = CardColor.Primary,
                        fillingType = FillingTypeUiModel.Filled,
                        shape = CardShape.Squiggle,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 2,
                        color = CardColor.Secondary,
                        fillingType = FillingTypeUiModel.Striped,
                        shape = CardShape.Oval,
                        selected = false,
                        isPortraitMode = isPortrait,
                    ),
                    Slot.CardUiModel(
                        patternAmount = 3,
                        color = CardColor.Tertiary,
                        fillingType = FillingTypeUiModel.Outlined,
                        shape = CardShape.Diamond,
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
