package com.team23.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.team23.ui.card.SetCard
import com.team23.ui.card.Slot
import com.team23.ui.debug.isDebug
import com.team23.ui.dialog.EndGameDialog
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.koinInject
import com.team23.ui.card.Slot.CardUiModel.Color as CardColor
import com.team23.ui.card.Slot.CardUiModel.Shape as CardShape

@Composable
fun GameScreen() {
    val gameVM = koinInject<GameViewModel>()
    LaunchedEffect(Unit) {
        gameVM.start()
    }
    val game by gameVM.gameUiModelFlow.collectAsState()

    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        GameScreen(
            game = game,
            gameUiEvent = gameVM.gameUiEvent,
            onAction = gameVM::onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = if (game.isPortrait) LocalSpacings.current.large else LocalSpacings.current.none)
        )

        if (isDebug()) {
            GameDebugSelectSetFAB(
                onAction = gameVM::onAction,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(all = LocalSpacings.current.largeIncreased)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScreen(
    game: GameUiModel,
    gameUiEvent: SharedFlow<GameUiEvent>,
    modifier: Modifier = Modifier,
    onAction: (GameAction) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val columnsCount = getGridColumnsCount(game.isPortrait)
    var slotWidthPx by remember { mutableStateOf(0) }
    var positionsByIndex: Map<Int, IntOffset> by remember { mutableStateOf(emptyMap()) }
    val resetNonce = remember { mutableIntStateOf(0) }
    val completionType = remember { mutableStateOf<GameCompletionType?>(null) }

    Box(modifier = modifier) {
        GameContainer(
            resetNonce = resetNonce.value,
            isVisible = !game.isLoading,
            columnsCount = columnsCount,
        ) {
            item(span = { GridItemSpan(columnsCount) }) {
                GameTimer(
                    timerValue = game.timer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = LocalSpacings.current.medium)
                )
            }
            itemsIndexed(
                items = game.playingCards,
                key = { _, slot -> slot.id },
            ) { index, slot ->
                Slot(
                    slot = slot,
                    isPortrait = game.isPortrait,
                    isSelectable = !game.isFinished,
                    onAction = onAction,
                    modifier = if (!game.hasAnimation) Modifier else Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 500, easing = LinearEasing),
                        placementSpec = tween(durationMillis = 500, easing = LinearEasing),
                    ).onSizeChanged { size ->
                        slotWidthPx = size.width
                    }.onGloballyPositioned { coords ->
                        val tempMap = positionsByIndex.toMutableMap()
                        tempMap[index] = coords.positionInRoot().round()
                        positionsByIndex = tempMap
                    }
                )
            }
            item(span = { GridItemSpan(columnsCount) }) {
                Text(
                    text = "${game.cardsInDeck.size} cards remaining in deck",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = LocalSpacings.current.small)
                )
            }
        }
        if (game.isLoading || (game.isFinished && completionType.value == null)) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        completionType.value?.let { completionType ->
            EndGameDialog(
                completionType = completionType,
                onAction = onAction,
            )
        }
    }

    val cardsToAnimate = remember { mutableStateOf(emptySet<Pair<IntOffset, Slot.CardUiModel>>()) }

    if (game.hasAnimation) {
        FlyOutAnimation(
            cards = cardsToAnimate.value,
            isPortrait = game.isPortrait,
            widthPx = slotWidthPx,
        )
    }

    fun handleAnimateCards(cardsWithIndex: Set<Pair<Int, Slot.CardUiModel>>) {
        cardsToAnimate.value = cardsWithIndex
            .map { (index, card) ->
                val initialPosition = positionsByIndex[index] ?: IntOffset(0, 0)
                initialPosition to card
            }.toSet()
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                gameUiEvent.collect { gameEvent ->
                    when (gameEvent) {
                        is GameUiEvent.AnimateSelectedCards -> handleAnimateCards(gameEvent.cardsWithIndex)
                        is GameUiEvent.ResetScreen -> {
                            resetNonce.value++
                            completionType.value = null
                        }

                        is GameUiEvent.GameCompletion -> completionType.value = gameEvent.type
                    }
                }
            }
        }
    }
}

@Composable
private fun GameContainer(
    resetNonce: Int,
    isVisible: Boolean,
    columnsCount: Int,
    content: LazyGridScope.() -> Unit,
) {
    key(resetNonce) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnsCount),
                contentPadding = PaddingValues(all = LocalSpacings.current.small),
                content = content,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
fun FlyOutAnimation(
    cards: Set<Pair<IntOffset, Slot.CardUiModel>>,
    isPortrait: Boolean,
    widthPx: Int,
) {
    val density = LocalDensity.current
    val (screenWidth, screenHeight) = LocalWindowInfo.current.containerSize
    val targetOffset = IntOffset(x = screenWidth + widthPx, y = screenHeight / 2)

    cards.forEach { (initialPosition, card) ->
        val offset = remember {
            Animatable(
                initialValue = initialPosition,
                typeConverter = IntOffset.VectorConverter
            )
        }
        val alpha = remember { Animatable(initialValue = 1f) }
        LaunchedEffect(card.id) {
            offset.snapTo(initialPosition)
            offset.animateTo(
                targetValue = targetOffset,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing),
            )
        }
        LaunchedEffect(card.id) {
            alpha.snapTo(1f)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing),
            )
        }

        Slot(
            slot = card,
            isPortrait = isPortrait,
            isSelectable = false,
            modifier = Modifier
                .width(with(density) { widthPx.toDp() })
                .absoluteOffset { offset.value }
                .alpha(alpha.value),
        )
    }
}

@Composable
private fun GameTimer(
    timerValue: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = timerValue,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
private fun Slot(
    slot: Slot,
    isPortrait: Boolean,
    isSelectable: Boolean,
    modifier: Modifier = Modifier,
    onAction: (GameAction) -> Unit = { },
) {
    when (slot) {
        is Slot.CardUiModel -> SetCard(
            card = slot,
            modifier = modifier
                .padding(all = LocalSpacings.current.small)
                .clip(MaterialTheme.shapes.small)
                .clickable(enabled = isSelectable) { onAction(GameAction.SelectOrUnselectCard(slot)) }
                .fillMaxWidth()
                .aspectRatio(getCardAspectRation(isPortrait)),
        )

        is Slot.HoleUiModel -> Box(
            modifier = modifier
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
private fun GameScreenPreview(@PreviewParameter(PortraitPreviewProvider::class) isPortrait: Boolean) {
    SetTheme {
        GameScreen(
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
            ),
            gameUiEvent = MutableSharedFlow(),
        )
    }
}

private class PortraitPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}
