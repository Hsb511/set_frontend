package com.team23.ui.settings

data class SettingsUiModel(
    val account: Account = Account(),
    val preferences: Preferences = Preferences(),
    val about: About = About(),
) {
    data class Account(
        val username: String = "",
        val userId: String = "",
    )

    data class Preferences(
        val cardPortrait: Boolean = true,
        val forceDarkMode: Boolean = false,
        val forceLightMode: Boolean = false,
    )

    data class About(
        val appVersion: String = "",
        val apiVersion: String? = null,
        val baseUrl: String? = null,
    )
}
