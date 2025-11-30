package com.team23.ui.settings

sealed interface SettingsAction {
    data object NavigateBack: SettingsAction
    data object Logout: SettingsAction
    data class ToggleCardOrientation(val currentValue: Boolean): SettingsAction
    data class ToggleForceDarkMode(val currentValue: Boolean): SettingsAction
    data class ToggleForceLightMode(val currentValue: Boolean): SettingsAction
}
