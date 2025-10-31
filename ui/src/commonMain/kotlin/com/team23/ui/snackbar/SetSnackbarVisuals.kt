package com.team23.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

sealed interface SetSnackbarVisuals: SnackbarVisuals {

    data object InvalidSet: SetSnackbarVisuals {
        override val message: String = "Not a set!"
        override val actionLabel: String? = null
        override val withDismissAction: Boolean = false
        override val duration: SnackbarDuration = SnackbarDuration.Short
    }

    data object CannotCreateGame: SetSnackbarVisuals {
        override val message: String = "An error occurred while creating the game"
        override val actionLabel: String? = null
        override val withDismissAction: Boolean = false
        override val duration: SnackbarDuration = SnackbarDuration.Short
    }
}
