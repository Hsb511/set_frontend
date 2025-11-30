package com.team23.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team23.ui.debug.isDebug
import com.team23.ui.dialog.LogoutDialog
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
        onAction = settingsViewModel::onAction,
        modifier = modifier,
    )
}

@Composable
private fun SettingsScreen(
    settingsUiModel: SettingsUiModel,
    modifier: Modifier = Modifier,
    onAction: (SettingsAction) -> Unit = {},
) {
    Scaffold(
        topBar = {
            SettingsTopBar(
                username = settingsUiModel.account.username,
                onAction = onAction,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.small),
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(all = LocalSpacings.current.large),
        ) {
            if (isDebug()) {
                SettingsAccountSection(settingsUiModel.account)

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = LocalSpacings.current.medium
                    )
                )

                SettingsPreferencesSection(
                    preferences = settingsUiModel.preferences,
                    onAction = onAction,
                )

                if (settingsUiModel.backend.isVisible) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = LocalSpacings.current.medium))

                    SettingsBackendSection(backend = settingsUiModel.backend)
                }
            }
        }
    }
}

@Composable
private fun SettingsAccountSection(account: SettingsUiModel.Account) {
    SettingsSectionTitle(
        label = "Account",
        modifier = Modifier
            .padding(bottom = LocalSpacings.current.medium)
    )

    SettingsRow(
        label = "userId",
        value = account.userId,
    )
}

@Composable
private fun SettingsPreferencesSection(
    preferences: SettingsUiModel.Preferences,
    onAction: (SettingsAction) -> Unit
) {
    SettingsSectionTitle(label = "Preferences")

    SettingsRowCheckBox(
        label = "Turn cards in portrait",
        checked = preferences.cardPortrait,
        onValueChanged = { onAction(SettingsAction.ToggleCardOrientation(preferences.cardPortrait)) }
    )


    SettingsRowCheckBox(
        label = "Force dark mode",
        checked = preferences.forceDarkMode,
        onValueChanged = { onAction(SettingsAction.ToggleForceDarkMode(preferences.forceDarkMode)) }
    )

    SettingsRowCheckBox(
        label = "Force light mode",
        checked = preferences.forceLightMode,
        onValueChanged = { onAction(SettingsAction.ToggleForceLightMode(preferences.forceLightMode)) }
    )
}

@Composable
private fun SettingsBackendSection(backend: SettingsUiModel.Backend) {
    SettingsSectionTitle(
        label = "Backend",
        modifier = Modifier
            .padding(bottom = LocalSpacings.current.medium)
    )

    backend.apiVersion?.let { apiVersion ->
        SettingsRow(label = "API", value = apiVersion)
    }
    backend.baseUrl?.let { baseUrl ->
        SettingsRow(label = "baseUrl", value = baseUrl)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(
    username: String,
    onAction: (SettingsAction) -> Unit,
) {
    var logoutDialogVisible by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = username) },
        navigationIcon = {
            IconButton(
                onClick = { onAction(SettingsAction.NavigateBack) }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Navigate back",
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    logoutDialogVisible = true
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Log out",
                )
            }
        }
    )

    if (logoutDialogVisible) {
        LogoutDialog(
            onDismiss = { logoutDialogVisible = false },
            onConfirm = {
                onAction(SettingsAction.Logout)
                logoutDialogVisible = false
            },
        )
    }
}

@Composable
private fun SettingsSectionTitle(
    label: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier,
    )
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
private fun SettingsRowCheckBox(
    label: String,
    checked: Boolean,
    onValueChanged: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .width(150.dp),
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onValueChanged,
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun SettingsScreenPreview() {
    SetTheme {
        SettingsScreen(
            settingsUiModel = SettingsUiModel(
                account = SettingsUiModel.Account(
                    username = "My username",
                    userId = "7a5b5f94-1a6b-4f4a-b92e-d8795c4e58a8",
                ),
                preferences = SettingsUiModel.Preferences(
                    cardPortrait = true,
                    forceDarkMode = true,
                ),
                backend = SettingsUiModel.Backend(
                    apiVersion = "SET Game Server v0.7.6",
                    baseUrl = "https://base_url-com",
                ),
            )
        )
    }
}
