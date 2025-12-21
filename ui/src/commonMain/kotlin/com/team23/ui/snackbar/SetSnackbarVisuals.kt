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

    data class CannotCreateGame(val errorMessage: String?): ShortSnackbarVisuals() {
        override val message: String = "An error occurred while creating the game: $errorMessage"
    }

    data class SignInError(val errorMessage: String?): ShortSnackbarVisuals() {
        override val message: String = "An error occurred while signing in: $errorMessage"
    }

    data class SignUpError(val errorMessage: String?): ShortSnackbarVisuals() {
        override val message: String = "An error occurred while signing up: $errorMessage"
    }

    data object GuestRegistrationError: ShortSnackbarVisuals() {
        override val message: String = "An error occurred while trying to register you as a guest"
    }

    data class DebugClearSuccess(override val message: String): ShortSnackbarVisuals()

    data class DebugClearFailure(val errorMessage: String?): ShortSnackbarVisuals() {
        override val message: String = "An error occurred while clearing: $errorMessage"
    }

    data object GameCompletionSuccess: ShortSnackbarVisuals() {
        override val message: String = "The game was properly saved in the server"
    }

    data class GameCompletionError(val errorMessage: String?): ShortSnackbarVisuals() {
        override val message: String = "The game did not properly complete: $errorMessage"
    }

    data class LogOutError(val errorMessage: String?): ShortSnackbarVisuals() {
        override val message: String = "Logging out didn't succeed: $errorMessage"
    }

    data class JoiningMultiGame(val hostName: String): ShortSnackbarVisuals() {
        override val message: String = "Joining game of $hostName"
    }

    data class FormatErrorMultiGameId(val gameId: String): ShortSnackbarVisuals() {
        override val message: String = "The format of gameId $gameId is not corret"
    }
}
