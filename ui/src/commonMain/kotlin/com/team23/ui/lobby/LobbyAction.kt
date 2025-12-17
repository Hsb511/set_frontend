package com.team23.ui.lobby

sealed interface LobbyAction {
    data object StartSolo : LobbyAction
    data object StartMulti : LobbyAction
}
