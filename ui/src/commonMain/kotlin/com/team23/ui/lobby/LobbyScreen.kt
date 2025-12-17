package com.team23.ui.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team23.ui.button.ActionButton
import com.team23.ui.debug.isDebug
import com.team23.ui.game.GameAction
import com.team23.ui.game.GameViewModel
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun LobbyScreen() {
    val gameViewModel = koinInject<GameViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        LobbyScreen(
            onAction = gameViewModel::onAction,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun LobbyScreen(
    modifier: Modifier = Modifier,
    onAction: (GameAction) -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.largeIncreased),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LocalSpacings.current.large),
    ) {
        Text(
            text = "Chose a game mode",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )

        ActionButton(
            text = "Solo",
            onClick = { onAction(GameAction.StartSolo) },
            modifier = Modifier.fillMaxWidth(),
        )

        ActionButton(
            text = "\uD83D\uDEA7 Multi \uD83D\uDEA7 ",
            enabled = isDebug(),
            onClick = { onAction(GameAction.StartMulti) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun LobbyScreenPreview() {
    SetTheme {
        LobbyScreen {}
    }
}
