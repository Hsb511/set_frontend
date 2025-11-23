package com.team23.ui.debug

sealed interface DebugUiEvent {
    data object LoadingStarted: DebugUiEvent
    data class LoadingFinished(val isSuccess: Boolean): DebugUiEvent
}
