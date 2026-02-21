package com.team23.ui.gameLobby

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.team23.ui.button.ActionButton
import com.team23.ui.button.ActionButtonUiModel
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.system.clipEntryOf
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun GameLobbyScreen(gameName: String?, multiGameMode: NavigationScreen.GameLobby.MultiGameMode) {
    val gameLobbyVM = koinInject<GameLobbyViewModel>()
    LaunchedEffect(Unit) {
        gameLobbyVM.start(gameName, multiGameMode)
    }

    when (val gameLobbyUiModel = gameLobbyVM.gameLobbyUiModel.collectAsState().value) {
        is GameLobbyUiModel.Data -> GameLobbyScreen(
            gameLobbyUiModel = gameLobbyUiModel,
            onAction = gameLobbyVM::onAction,
        )

        is GameLobbyUiModel.Loading -> Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Multi game data are being loaded")
            CircularProgressIndicator()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val clipboard = LocalClipboard.current

    suspend fun copyToClipboard(text: String) {
        val clipEntry = clipEntryOf(text)
        clipboard.setClipEntry(clipEntry)
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                gameLobbyVM.gameLobbyUiEvent.collect { gameLobbyUiEvent ->
                    when (gameLobbyUiEvent) {
                        is GameLobbyUiEvent.CopyToClipboard -> copyToClipboard(gameLobbyUiEvent.text)
                    }
                }
            }
        }
    }
}

@Composable
private fun GameLobbyScreen(
    gameLobbyUiModel: GameLobbyUiModel.Data,
    onAction: (GameLobbyAction) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(all = LocalSpacings.current.large),
    ) {
        TitleAndGameIdSection(
            gameLobbyUiModel = gameLobbyUiModel,
            onGameIdClicked = { onAction(GameLobbyAction.CopyGameId(gameLobbyUiModel.gameName)) }
        )

        VisibilitySection(
            isPrivate = gameLobbyUiModel.isPrivate,
            isHost = gameLobbyUiModel.isHost,
            onChangeVisibility = { isPrivate -> onAction(GameLobbyAction.ChangeVisibility(isPrivate)) },
        )

        PlayersSection(allPlayers = gameLobbyUiModel.allPlayers)

        Spacer(Modifier.weight(1f))

        GameActionButton(
            isHost = gameLobbyUiModel.isHost,
            onStartGame = { onAction(GameLobbyAction.StartGame) },
            onLeaveGame = { onAction(GameLobbyAction.LeaveGame) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun TitleAndGameIdSection(
    gameLobbyUiModel: GameLobbyUiModel.Data,
    onGameIdClicked: () -> Unit,
) {
    Text(
        text = if (gameLobbyUiModel.isHost) {
            "You created this game, share this id to other players: "
        } else {
            "You joined the game created by ${gameLobbyUiModel.hostUsername}, whose id is:"
        },
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalSpacings.current.medium),
        modifier = Modifier
            .height(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.small,
            )
            .clip(shape = MaterialTheme.shapes.small)
            .clickable { onGameIdClicked() }
            .padding(horizontal = LocalSpacings.current.medium)
    ) {
        Text(
            text = gameLobbyUiModel.gameName,
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Justify
            ),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Outlined.ContentCopy,
            contentDescription = "Copy button",
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun VisibilitySection(
    isPrivate: Boolean,
    isHost: Boolean,
    onChangeVisibility: (Boolean) -> Unit,
) {
    Column {
        val commonVisibilityText =
            if (isPrivate) "The game is private, it can only be joined with the game id." else "The game is public, anyone can join it from the main page."
        val visibilityText = if (isHost) "$commonVisibilityText Change visibility: " else commonVisibilityText
        Text(
            text = visibilityText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (isHost) {
            VisibilityRadioRow(
                selected = isPrivate,
                label = "Make it private",
                onClick = { onChangeVisibility(true) },
            )
            VisibilityRadioRow(
                selected = !isPrivate,
                label = "Make it public",
                onClick = { onChangeVisibility(false) },
            )
        }
    }
}

@Composable
private fun VisibilityRadioRow(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun PlayersSection(
    allPlayers: List<GameLobbyUiModel.Data.Player>
) {
    Text(
        text = "Players in the lobby:",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
    allPlayers.forEach { player ->
        GamePlayerRow(
            player = player,
        )
    }
}

@Composable
private fun GamePlayerRow(
    player: GameLobbyUiModel.Data.Player
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (player.isHost) {
            Icon(
                imageVector = Icons.Outlined.PersonPin,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.width(48.dp)
            )
        } else if (player.isYou) {
            Text(
                text = "You",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(48.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(48.dp))
        }

        Text(
            text = player.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun GameActionButton(
    isHost: Boolean,
    onStartGame: () -> Unit,
    onLeaveGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isHost) {
        ActionButton(
            uiModel = ActionButtonUiModel(
                text = "Start game"
            ),
            onClick = onStartGame,
            modifier = modifier
        )
    } else {
        ActionButton(
            uiModel = ActionButtonUiModel(
                text = "Leave game",
            ),
            onClick = onLeaveGame,
            modifier = modifier
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameLobbyPreview(
    @PreviewParameter(GameLobbyPreviewProvider::class) gameLobbyUiModel: GameLobbyUiModel.Data,
) {
    SetTheme {
        GameLobbyScreen(
            gameLobbyUiModel = gameLobbyUiModel,
        )
    }
}

private class GameLobbyPreviewProvider : PreviewParameterProvider<GameLobbyUiModel> {

    private val gameId = "12345678-1234-1234-1234-1234567890ab"

    override val values: Sequence<GameLobbyUiModel.Data> = sequenceOf(
        GameLobbyUiModel.Data(
            gameName = gameId,
            isHost = true,
            isPrivate = false,
            hostUsername = "Guest#12345678",
            allPlayers = listOf(
                GameLobbyUiModel.Data.Player("Guest#12345678", isHost = true, isYou = true),
                GameLobbyUiModel.Data.Player("JohnDoe"),
                GameLobbyUiModel.Data.Player("JaneDoe"),
                GameLobbyUiModel.Data.Player("Wow23"),
            )
        ),
        GameLobbyUiModel.Data(
            gameName = gameId,
            isHost = false,
            isPrivate = true,
            hostUsername = "Guest#12345678",
            allPlayers = listOf(
                GameLobbyUiModel.Data.Player("Guest#12345678", isHost = true),
                GameLobbyUiModel.Data.Player("JohnDoe"),
                GameLobbyUiModel.Data.Player("JaneDoe"),
                GameLobbyUiModel.Data.Player("Wow23", isYou = true),
            )
        )
    )
}
