package com.team23.ui.snackbar

import androidx.compose.material3.SnackbarVisuals
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object SnackbarManager {

    private val _snackbar: MutableSharedFlow<SnackbarVisuals> = MutableSharedFlow()
    val snackbar: SharedFlow<SnackbarVisuals> = _snackbar

    suspend fun showMessage(snackbarVisuals: SnackbarVisuals) {
        _snackbar.emit(snackbarVisuals)
    }
}
