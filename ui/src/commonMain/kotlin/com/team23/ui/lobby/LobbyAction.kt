package com.team23.ui.lobby

sealed interface LobbyAction {
    data class CreateSolo(val hasAnOngoingSoloGame: Boolean) : LobbyAction
    data object ContinueSolo : LobbyAction
    data object StartMulti : LobbyAction
}
