package com.team23.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.team23.ui.game.EndGameUiModel
import com.team23.ui.game.GameAction
import com.team23.ui.theming.LocalSpacings

@Composable
fun EndGameDialog(
    endGameUiModel: EndGameUiModel,
    onAction: (GameAction) -> Unit = {},
) {
    when (endGameUiModel.content) {
        is EndGameUiModel.Content.Action -> EndGameDialogWithAction(endGameUiModel.content, endGameUiModel.label, onAction)
        is EndGameUiModel.Content.Loader -> EndGameDialogWithLoader(endGameUiModel.label)
    }
}

@Composable
private fun EndGameDialogWithAction(
    content: EndGameUiModel.Content.Action,
    actionLabel: String,
    onAction: (GameAction) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(
                onClick = { onAction(content.action) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = actionLabel)
            }
        },
        dismissButton = {
            Button(
                onClick = { onAction(GameAction.ChangeGameType) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Go back to lobby")
            }
        },
        title = {
            Text(
                text = "Game is finished",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        modifier = Modifier.width(280.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EndGameDialogWithLoader(
    dialogDescription: String,
) {
    BasicAlertDialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = LocalSpacings.current.extraLarge),
            ) {
                Text(
                    text = "Game is finished",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = dialogDescription,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                )
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = LocalSpacings.current.large)
                )
            }
        }
    }
}
