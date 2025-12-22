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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team23.ui.button.ActionButton
import com.team23.ui.button.ActionButtonUiModel
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun GameLobbyScreen(
    gameLobbyUiModel: GameLobbyUiModel,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.large),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(all = LocalSpacings.current.large),
    ) {
        Text(
            text = if (gameLobbyUiModel.isHost) {
                "You created this game, share this id to other players: "
            } else {
                "You joined the game created by ${gameLobbyUiModel.hostUsername}, whose id is:"
            },
            style = MaterialTheme.typography.titleLarge,
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
                .clickable { /* TODO */ }
                .padding(horizontal = LocalSpacings.current.medium)
        ) {
            Text(
                text = gameLobbyUiModel.gameId,
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
        Text(
            text = "Players in the lobby:",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        gameLobbyUiModel.allPlayers.forEach { player ->
            GamePlayerRow(
                player = player,
            )
        }
        Spacer(Modifier.weight(1f))
        GameActionButton(
            isHost = gameLobbyUiModel.isHost,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun GamePlayerRow(
    player: GameLobbyUiModel.Player
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (player.isHost) {
            Icon(
                imageVector = Icons.Outlined.PersonPin,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.width(40.dp)
            )
        } else if (player.isYou) {
            Text(
                text = "You",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(40.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(40.dp))
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
    modifier: Modifier = Modifier
) {
    if (isHost) {
        ActionButton(
            uiModel = ActionButtonUiModel(
                text = "Start game"
            ),
            onClick = { /* TODO */ },
            modifier = modifier
        )
    } else {
        ActionButton(
            uiModel = ActionButtonUiModel(
                text = "Waiting for host to start game",
                enabled = false,
            ),
            onClick = { },
            modifier = modifier
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameLobbyPreview(
    @PreviewParameter(GameLobbyPreviewProvider::class) gameLobbyUiModel: GameLobbyUiModel,
) {
    SetTheme {
        GameLobbyScreen(
            gameLobbyUiModel = gameLobbyUiModel,
        )
    }
}

private class GameLobbyPreviewProvider : PreviewParameterProvider<GameLobbyUiModel> {

    private val gameId = "12345678-1234-1234-1234-1234567890ab"

    override val values: Sequence<GameLobbyUiModel> = sequenceOf(
        GameLobbyUiModel(
            gameId = gameId,
            isHost = true,
            isPrivate = false,
            hostUsername = "Guest#12345678",
            allPlayers = listOf(
                GameLobbyUiModel.Player("Guest#12345678", isHost = true, isYou = true),
                GameLobbyUiModel.Player( "JohnDoe"),
                GameLobbyUiModel.Player("JaneDoe"),
                GameLobbyUiModel.Player("Wow23"),
            )
        ),
        GameLobbyUiModel(
            gameId = gameId,
            isHost = false,
            isPrivate = true,
            hostUsername = "Guest#12345678",
            allPlayers = listOf(
                GameLobbyUiModel.Player("Guest#12345678", isHost = true),
                GameLobbyUiModel.Player( "JohnDoe"),
                GameLobbyUiModel.Player("JaneDoe"),
                GameLobbyUiModel.Player("Wow23", isYou = true),
            )
        )
    )
}
