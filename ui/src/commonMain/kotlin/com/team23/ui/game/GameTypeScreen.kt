package com.team23.ui.game

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
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun GameTypeScreen() {
    val gameViewModel = koinInject<GameViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        GameTypeScreen(
            onAction = gameViewModel::onAction,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun GameTypeScreen(
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
            text = "Multi",
            enabled = false,
            onClick = { onAction(GameAction.StartMulti) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameTypeScreenPreview() {
    SetTheme {
        GameTypeScreen {}
    }
}
