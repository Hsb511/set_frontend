package com.team23.ui.debug

import com.team23.domain.admin.AdminClearMode
import com.team23.domain.admin.AdminRepository
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class DebugViewModel(
    private val adminRepository: AdminRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {

    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)

    private val _uiEvent: MutableSharedFlow<DebugUiEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<DebugUiEvent> = _uiEvent

    fun onAction(action: DebugAction) {
        val mode = when (action) {
            is DebugAction.ClearDb -> AdminClearMode.Database
            is DebugAction.ClearMemory -> AdminClearMode.AllMemory
            is DebugAction.ClearGames -> AdminClearMode.GamesOnly
        }
        viewModelScope.launch {
            _uiEvent.emit(DebugUiEvent.LoadingStarted)
            adminRepository.clear(mode)
                .onSuccess { message ->
                    _uiEvent.emit(DebugUiEvent.LoadingFinished(isSuccess = true))
                    SnackbarManager.showMessage(SetSnackbarVisuals.DebugClearSuccess(message))
                }
                .onFailure { throwable ->
                    _uiEvent.emit(DebugUiEvent.LoadingFinished(isSuccess = false))
                    SnackbarManager.showMessage(SetSnackbarVisuals.DebugClearFailure(throwable.message))
                }
        }
    }
}
