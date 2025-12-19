package com.team23.ui.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team23.ui.button.ActionButton
import com.team23.ui.button.ActionButtonUiModel
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun LobbyScreen() {
    val lobbyViewModel = koinInject<LobbyViewModel>()

    LobbyScreen(
        // TODO USE VM
        lobbyUiModel = LobbyUiModel(
            hasAnOngoingSoloGame = true,
            multiGames = List(23) {
                val gameId = Uuid.random()
                LobbyUiModel.MultiGame(
                    gameId = gameId,
                    hostName = "Guest#${gameId.toString().take(8)}",
                    playersCount = it,
                )
            },
        ),
        onAction = lobbyViewModel::onAction,
    )
}

@Composable
private fun LobbyScreen(
    lobbyUiModel: LobbyUiModel,
    modifier: Modifier = Modifier,
    onAction: (LobbyAction) -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        modifier = modifier
            .fillMaxSize()
            .padding(all = LocalSpacings.current.large),
    ) {
        Text(
            text = "SOLO",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "You have ${if (lobbyUiModel.hasAnOngoingSoloGame) "an" else "no"} active solo game:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(LocalSpacings.current.large)) {
            if (lobbyUiModel.hasAnOngoingSoloGame) {
                ActionButton(
                    uiModel = ActionButtonUiModel(
                        text = "Continue",
                        size = ActionButtonUiModel.Size.Small,
                    ),
                    onClick = { onAction(LobbyAction.StartSolo) },
                    modifier = Modifier.weight(1f),
                )
            }
            ActionButton(
                uiModel = ActionButtonUiModel(
                    text = "${if (lobbyUiModel.hasAnOngoingSoloGame) "\uD83D\uDEA7" else ""} Create new ${if (lobbyUiModel.hasAnOngoingSoloGame) "\uD83D\uDEA7" else ""}",
                    size = ActionButtonUiModel.Size.Small,
                ),
                onClick = { onAction(LobbyAction.StartSolo) },
                modifier = Modifier.weight(1f),
            )
        }

        HorizontalDivider()

        Text(
            text = "\uD83D\uDEA7 MULTI \uD83D\uDEA7",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        ActionButton(
            uiModel = ActionButtonUiModel(
                text = "\uD83D\uDEA7 Create new multi game \uD83D\uDEA7",
                size = ActionButtonUiModel.Size.Small,
            ),
            onClick = {  },
            modifier = Modifier.fillMaxWidth(),
        )

        ActionButton(
            uiModel = ActionButtonUiModel(
                text = "\uD83D\uDEA7 Join multi game \uD83D\uDEA7",
                size = ActionButtonUiModel.Size.Small,
            ),
            onClick = {  },
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "\uD83D\uDEA7 Ongoing games:\uD83D\uDEA7",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        if (lobbyUiModel.hasMultiGames) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(all = LocalSpacings.current.small),
                modifier = Modifier
                    .padding(horizontal = LocalSpacings.current.large)
            ) {
                stickyHeader {
                    LobbyMultiTableHeader()
                }
                items(lobbyUiModel.multiGames) { multiGame ->
                    LobbyMultiTableItem(multiGame)
                }
            }
        }
    }
}

@Composable
private fun LobbyMultiTableHeader() {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Row {
            Text(
                text = "Host",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.width(firstColumnWidth())
            )
            Text(
                text = "Players",
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        HorizontalDivider(thickness = 2.dp)
    }
}

@Composable
private fun LobbyMultiTableItem(multiGame: LobbyUiModel.MultiGame) {
    Column {
        Row(
            modifier = Modifier
                .padding(
                    vertical = LocalSpacings.current.medium,
                    horizontal = LocalSpacings.current.small
                ),
        ) {
            Text(
                text = multiGame.hostName,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.width(firstColumnWidth())
            )
            Text(
                text = multiGame.playersCount.toString(),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun firstColumnWidth(): Dp {
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(LocalDensity.current) { screenWidthPx.toDp() }
    return screenWidthDp / 2f
}

@Composable
@Preview(showBackground = true)
private fun LobbyScreenPreview(
    @PreviewParameter(LobbyPreviewProvider::class) lobbyUiModel: LobbyUiModel,
) {
    SetTheme {
        LobbyScreen(
            lobbyUiModel = lobbyUiModel,
        ) {}
    }
}

@OptIn(ExperimentalUuidApi::class)
private class LobbyPreviewProvider : PreviewParameterProvider<LobbyUiModel> {
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

    override val values: Sequence<LobbyUiModel> = sequenceOf(
        LobbyUiModel(
            hasAnOngoingSoloGame = true,
            multiGames = fixedRandomUuids.map { gameId ->
                LobbyUiModel.MultiGame(
                    gameId = Uuid.parse(gameId),
                    hostName = "Guest#${gameId.take(8)}",
                    playersCount = gameId.last().digitToInt(),
                )
            },
        ),
        LobbyUiModel(
            hasAnOngoingSoloGame = false,
            multiGames = emptyList(),
        ),
    )
}
