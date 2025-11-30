package com.team23.ui.settings

sealed interface SettingsAction {
    data object NavigateBack: SettingsAction
    data object Logout: SettingsAction
}