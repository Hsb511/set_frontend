package com.team23.ui.lobby

sealed interface LobbyAction {
    data object CreateSolo : LobbyAction
    data object ContinueSolo : LobbyAction
    data object StartMulti : LobbyAction
}
