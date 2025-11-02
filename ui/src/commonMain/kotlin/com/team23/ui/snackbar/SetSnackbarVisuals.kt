package com.team23.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

sealed interface SetSnackbarVisuals: SnackbarVisuals {

    sealed class ShortSnackbarVisuals: SetSnackbarVisuals {
        override val actionLabel: String? = null
        override val withDismissAction: Boolean = false
        override val duration: SnackbarDuration = SnackbarDuration.Short
    }

    data object InvalidSet: ShortSnackbarVisuals() {
        override val message: String = "Not a set!"
    }

    data object CannotCreateGame: ShortSnackbarVisuals() {
        override val message: String = "An error occurred while creating the game"
    }

    data class NoUuidFormat(val userId: String): ShortSnackbarVisuals() {
        override val message: String = "Expected either a 36-char string in the standard hex-and-dash UUID format or a 32-char hexadecimal string, but was \"$userId\""
    }

    data object SignInError: ShortSnackbarVisuals() {
        override val message: String = "An error occurred while signing in"
    }

    data object SignUpError: ShortSnackbarVisuals() {
        override val message: String = "An error occurred while signing up"
    }

    data object DeviceRegistration: ShortSnackbarVisuals() {
        override val message: String = "An error occurred while linking your device to your account"
    }
}
