package com.team23.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {
    val settingsViewModel = koinInject<SettingsViewModel>()
    val settingsUiModel by settingsViewModel.settingsFlow.collectAsState()

    SettingsScreen(
        settingsUiModel = settingsUiModel,
        modifier = modifier,
    )
}

@Composable
private fun SettingsScreen(
    settingsUiModel: SettingsUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.small),
        modifier = modifier,
    ) {
        SettingsRow(label = "username", value = settingsUiModel.username)
        SettingsRow(label = "userId", value = settingsUiModel.userId)
    }
}

@Composable
private fun SettingsRow(
    label: String,
    value: String,
) {
    Row {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(70.dp),
        )
        SelectionContainer {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
@Preview
private fun SettingsScreenPreview() {
    SetTheme {
        SettingsScreen(
            settingsUiModel = SettingsUiModel(
                username = "My username",
                userId = "7a5b5f94-1a6b-4f4a-b92e-d8795c4e58a8",
            )
        )
    }
}
