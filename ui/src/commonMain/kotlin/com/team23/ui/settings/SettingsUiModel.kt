package com.team23.ui.settings

data class SettingsUiModel(
    val account: Account = Account(),
    val preferences: Preferences = Preferences(),
    val backend: Backend = Backend(),
) {
    data class Account(
        val username: String = "",
        val userId: String = "",
    )

    data class Preferences(
        val cardPortrait: Boolean = false,
        val forceDarkMode: Boolean = false,
        val forceLightMode: Boolean = false,
    )

    data class Backend(
        val apiVersion: String? = null,
        val baseUrl: String? = null,
    ) {
        val isVisible: Boolean = apiVersion != null || baseUrl != null
    }
}
