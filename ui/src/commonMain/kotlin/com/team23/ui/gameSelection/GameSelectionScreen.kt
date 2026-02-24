package com.team23.ui.gameSelection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team23.ui.button.ActionButton
import com.team23.ui.button.ActionButtonUiModel
import com.team23.ui.component.AdaptativeContainer
import com.team23.ui.gameSelection.GameSelectionUiModel.Data.MultiGame
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import com.team23.ui.theming.WindowSize
import com.team23.ui.theming.rememberWindowSize
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun GameSelectionScreen() {
    val lobbyViewModel = koinInject<GameSelectionViewModel>()
    LaunchedEffect(Unit) {
        lobbyViewModel.onStart()
    }

    when (val lobbyUiModel = lobbyViewModel.gameSelectionUiModel.collectAsStateWithLifecycle().value) {
        is GameSelectionUiModel.Loading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }

        is GameSelectionUiModel.Data -> GameSelectionScreen(
            lobbyUiModel = lobbyUiModel,
            onAction = lobbyViewModel::onAction,
        )
    }
}

@Composable
private fun GameSelectionScreen(
    lobbyUiModel: GameSelectionUiModel.Data,
    modifier: Modifier = Modifier,
    onAction: (GameSelectionAction) -> Unit,
) {

    val windowSize = rememberWindowSize()

    AdaptativeContainer(
        horizontal = windowSize == WindowSize.PhoneInLandscape,
        arrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = windowSize.getHorizontalGutter(),
                end = windowSize.getHorizontalGutter(),
                top = windowSize.getVerticalGutter(),
            )
    ) {
        SoloSection(
            hasOngoingSoloGame = lobbyUiModel.hasAnOngoingSoloGame,
            buttonsHorizontal = windowSize != WindowSize.PhoneInLandscape,
            modifier = soloSectionModifier(windowSize, lobbyUiModel.hasMultiGames),
            onAction = onAction,
        )

        if (windowSize != WindowSize.PhoneInLandscape) {
            HorizontalDivider()
        } else {
            VerticalDivider()
        }

        MultiSection(
            lobbyUiModel = lobbyUiModel,
            windowSize = windowSize,
            onAction = onAction,
        )
    }
}

@Composable
fun soloSectionModifier(
    windowSize: WindowSize,
    hasMultiGames: Boolean,
): Modifier {
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidth = with(LocalDensity.current) { screenWidthPx.toDp() }
    return when (windowSize) {
        WindowSize.PhoneInLandscape if hasMultiGames -> Modifier.width(screenWidth / 4f)
        WindowSize.PhoneInLandscape if !hasMultiGames -> Modifier.width(screenWidth / 3f)
        else -> Modifier.fillMaxWidth()
    }
}

@Composable
private fun SoloSection(
    hasOngoingSoloGame: Boolean,
    buttonsHorizontal: Boolean,
    modifier: Modifier = Modifier,
    onAction: (GameSelectionAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        modifier = modifier,
    ) {
        Text(
            text = "SOLO",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "You have ${if (hasOngoingSoloGame) "an" else "no"} active solo game:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        AdaptativeContainer(
            horizontal = buttonsHorizontal,
            arrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        ) {
            val buttonModifier = if (buttonsHorizontal) Modifier.weight(1f) else Modifier.fillMaxWidth()
            if (hasOngoingSoloGame) {
                ActionButton(
                    uiModel = ActionButtonUiModel(
                        text = "Continue",
                        size = ActionButtonUiModel.Size.Small,
                    ),
                    onClick = { onAction(GameSelectionAction.StartSolo(forceCreate = false)) },
                    modifier = buttonModifier,
                )
            }
            ActionButton(
                uiModel = ActionButtonUiModel(
                    text = "Create new",
                    size = ActionButtonUiModel.Size.Small,
                ),
                onClick = { onAction(GameSelectionAction.StartSolo(forceCreate = hasOngoingSoloGame)) },
                modifier = buttonModifier,
            )
        }
    }
}

@Composable
private fun MultiSection(
    lobbyUiModel: GameSelectionUiModel.Data,
    windowSize: WindowSize,
    onAction: (GameSelectionAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large)
    ) {
        Text(
            text = "\uD83D\uDEA7 MULTI \uD83D\uDEA7",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        AdaptativeContainer(
            horizontal = windowSize in listOf(WindowSize.PhoneInLandscape, WindowSize.TabletInLandscape),
            arrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        ) {
            MultiCreateAndJoinSection(
                windowSize = windowSize,
                onAction = onAction,
            )

            if (lobbyUiModel.hasMultiGames) {
                MultiPublicGamesSection(
                    windowSize = windowSize,
                    multiGames = lobbyUiModel.multiGames,
                    onAction = onAction,
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun MultiCreateAndJoinSection(
    windowSize: WindowSize,
    onAction: (GameSelectionAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        modifier = multiCreateAndJoinSectionModifier(windowSize),
    ) {
        CreateMultiActionButtons(
            windowSize = windowSize,
            onAction = onAction,
        )

        var multiGameName by remember { mutableStateOf("") }
        val isMultiGameNameValid = Regex("[A-Z]{6}").matches(multiGameName)

        TextField(
            value = multiGameName,
            onValueChange = { multiGameName = it },
            supportingText = {
                Text(text = "6 uppercase letters")
            },
            label = {
                Text(
                    text = "Enter/paste name to join game",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
        )

        ActionButton(
            uiModel = ActionButtonUiModel(
                text = "\uD83D\uDEA7 Join multi game \uD83D\uDEA7",
                size = ActionButtonUiModel.Size.Small,
                enabled = isMultiGameNameValid,
                maxLines = 1,
            ),
            onClick = { onAction(GameSelectionAction.JoinMulti(multiGameName)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun MultiPublicGamesSection(
    windowSize: WindowSize,
    multiGames: List<MultiGame>,
    onAction: (GameSelectionAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large),
    ) {
        Text(
            text = "\uD83D\uDEA7 Ongoing games:\uD83D\uDEA7",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(all = LocalSpacings.current.small),
            modifier = multiGamesModifier(windowSize)
        ) {
            stickyHeader {
                GameSelectionMultiTableHeader()
            }
            items(multiGames) { multiGame ->
                GameSelectionMultiTableItem(multiGame, onAction)
            }
        }
    }
}

@Composable
private fun multiCreateAndJoinSectionModifier(windowSize: WindowSize): Modifier {
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidth = with(LocalDensity.current) { screenWidthPx.toDp() }
    return when (windowSize) {
        WindowSize.PhoneInLandscape -> Modifier.width(screenWidth * (3f / 8f))
        WindowSize.TabletInLandscape -> Modifier.width(screenWidth / 2f)

        else -> Modifier.fillMaxWidth()
    }
}

@Composable
private fun multiGamesModifier(windowSize: WindowSize): Modifier {
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidth = with(LocalDensity.current) { screenWidthPx.toDp() }
    return when (windowSize) {
        WindowSize.PhoneInLandscape -> Modifier.width(screenWidth * (3f / 8f))
        WindowSize.TabletInLandscape -> Modifier.width(screenWidth / 2f)

        else -> Modifier.fillMaxWidth()
    }
}

@Composable
fun CreateMultiActionButtons(
    windowSize: WindowSize,
    onAction: (GameSelectionAction) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(LocalSpacings.current.large)) {
        CreateMultiButtonWithDescription(
            buttonLabel = "Create Time trial",
            description = "Each player has the same game in parallel. The fastest wins",
            windowSize = windowSize,
            onClick = { onAction(GameSelectionAction.CreateTimeTrial) },
            modifier = Modifier.weight(1f),
        )
        CreateMultiButtonWithDescription(
            buttonLabel = "Create Versus",
            description = "All the players play the same game synchronously. The one who finds most sets wins",
            windowSize = windowSize,
            onClick = { onAction(GameSelectionAction.CreateVersus) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CreateMultiButtonWithDescription(
    buttonLabel: String,
    description: String,
    windowSize: WindowSize,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.small),
        modifier = modifier
    ) {
        ActionButton(
            uiModel = ActionButtonUiModel(
                text = buttonLabel,
                size = ActionButtonUiModel.Size.Small,
                maxLines = 1,
            ),
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = if (windowSize == WindowSize.PhoneInLandscape) 2 else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun GameSelectionMultiTableHeader() {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Row {
            Text(
                text = "Name",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(FIRST_COLUMN_WEIGHT),
            )
            Text(
                text = "Players",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(SECOND_COLUMN_WEIGHT),
            )
            Text(
                text = "Type",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(THIRD_COLUMN_WEIGHT),
            )
        }
        HorizontalDivider(thickness = 2.dp)
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun GameSelectionMultiTableItem(multiGame: MultiGame, onAction: (GameSelectionAction) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onAction(GameSelectionAction.JoinMulti(publicName = multiGame.publicName))
                }
                .padding(
                    vertical = LocalSpacings.current.medium,
                    horizontal = LocalSpacings.current.small
                ),
        ) {
            Text(
                text = multiGame.publicName,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(FIRST_COLUMN_WEIGHT),
            )
            Text(
                text = multiGame.playersCount.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(SECOND_COLUMN_WEIGHT),
            )
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(THIRD_COLUMN_WEIGHT),
            ) {
                Icon(
                    imageVector = multiGame.gameMode.icon,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = multiGame.gameMode.name,
                )   
            }
        }
        HorizontalDivider()
    }
}

private const val FIRST_COLUMN_WEIGHT = 0.5f
private const val SECOND_COLUMN_WEIGHT = 0.25f
private const val THIRD_COLUMN_WEIGHT = 0.25f

@Composable
@Preview(showBackground = true)
private fun GameSelectionScreenPreview(
    @PreviewParameter(GameSelectionPreviewProvider::class) lobbyUiModel: GameSelectionUiModel.Data,
) {
    SetTheme {
        GameSelectionScreen(
            lobbyUiModel = lobbyUiModel,
        ) {}
    }
}

@OptIn(ExperimentalUuidApi::class)
private class GameSelectionPreviewProvider : PreviewParameterProvider<GameSelectionUiModel.Data> {
    private val fixedRandomUuids = listOf(
        "3f2c1c8a-9a6e-4a4a-b7b7-4c1e1b7e8f01",
        "a1d4e6c9-7b9c-4b2f-9c9f-1e8c2b4a6d32",
        "6e2f8a44-2b5c-4c8d-8b41-0f7d6a9c2e11",
        "d9c7a2b4-1f8e-4f44-9a6c-3e1b2d8f0a55",
        "4b8f3d2e-6c4a-45c7-8f9b-1a2e3c4d5e66",
        "b5a7d8e2-9c3f-4b1a-a4c8-7e6d5f2a9b77",
        "8c2a9f6e-4d7b-46e3-9a5c-1b8d2f4a3c88",
        "f1e4d3c9-7a6b-4c2f-8b5e-9a2d1c6f4e99",
        "2a9c7f5e-8d4b-4c1e-9f6a-b3d2e1a4c011",
        "c6b8d1a4-9f2e-4a7c-8e3b-5d9c1f2a3112",
        "7f5a9d2c-1b4e-4f8c-9e6a-3d2b1c4a5e13",
        "9e4c8d7a-5b2f-4a1e-8c6d-f3b2a1e4c214",
        "1c6b4e9d-8a5f-4c7e-9b2d-a3f1e2c4d315",
        "e7d9a5b2-4c6f-4e8a-9d1c-3b2f1a4c5e16",
        "5a1e9c2d-7f8b-4e4a-9c6d-2b3f1a8e4172",
        "b9f4e3a2-6c1d-4a7e-8c5b-2d1f9a3e6181",
        "d3c8a9b4-2f6e-4d1a-9e5c-b7f1a2c4e719",
        "4e2f7d9c-1a8b-4c6e-9a5d-b3f2c1e4a820",
        "8d1c6a5f-4e9b-4b2c-9a7e-f3d2c1a5b921",
        "c2a4f9b7-5e1d-4e8a-9c6b-3f1a2d4c1022",
        "9b3e7a2c-4f6d-4c8a-9e1b-d5f2a1c4e122",
        "f5c2d9e4-8a6b-4b1c-9a7e-3f2d1c4a1424",
        "1a8c4f2d-6b5e-4e9a-9c7d-f3a2b1c4e155",
    )

    override val values: Sequence<GameSelectionUiModel.Data> = sequenceOf(
        GameSelectionUiModel.Data(
            hasAnOngoingSoloGame = true,
            multiGames = fixedRandomUuids.map { gameId ->
                val playersCount = gameId.last().digitToInt()
                MultiGame(
                    // gameId = Uuid.parse(gameId),
                    publicName = gameId.take(6),
                    playersCount = playersCount,
                    gameMode = if (playersCount % 2 == 0) MultiGameMode.TimeTrial else MultiGameMode.Versus,
                )
            },
        ),
        GameSelectionUiModel.Data(
            hasAnOngoingSoloGame = false,
            multiGames = emptyList(),
        ),
    )
}
