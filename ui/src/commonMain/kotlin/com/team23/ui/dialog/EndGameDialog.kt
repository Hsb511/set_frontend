package com.team23.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.team23.ui.game.GameAction
import com.team23.ui.game.GameCompletionType

@Composable
fun EndGameDialog(
    completionType: GameCompletionType,
    onAction: (GameAction) -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = { },
        dismissButton = {
            Button(
                onClick = { onAction(GameAction.ChangeGameType) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Change game mode")
            }
        },
        confirmButton = {
            Button(
                onClick = { onAction(completionType.action) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = completionType.label)
            }
        },
        text = {
            Text(
                text = "Game is finished",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
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
