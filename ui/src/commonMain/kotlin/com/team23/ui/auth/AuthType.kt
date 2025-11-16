package com.team23.ui.auth

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

enum class AuthType(val label: String) {
    SignUp("sign up"),
    SignIn("sign in");

    val capitalizedLabel
        get() = label.capitalize(Locale.current)
}
