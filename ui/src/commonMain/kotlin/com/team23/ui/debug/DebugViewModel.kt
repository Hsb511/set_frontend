package com.team23.ui.debug

import com.team23.domain.admin.AdminClearMode
import com.team23.domain.admin.AdminRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DebugViewModel(
    private val adminRepository: AdminRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {

    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)

    fun onAction(action: DebugAction) {
        val mode = when (action) {
            is DebugAction.ClearDb -> AdminClearMode.Database
            is DebugAction.ClearMemory -> AdminClearMode.AllMemory
            is DebugAction.ClearGames -> AdminClearMode.GamesOnly
        }
        viewModelScope.launch {
            adminRepository.clear(mode)
        }
    }
}
