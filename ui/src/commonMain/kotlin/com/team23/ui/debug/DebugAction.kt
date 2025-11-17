package com.team23.ui.debug

sealed interface DebugAction {
    data object ClearGames: DebugAction
    data object ClearMemory: DebugAction
    data object ClearDb: DebugAction
}
