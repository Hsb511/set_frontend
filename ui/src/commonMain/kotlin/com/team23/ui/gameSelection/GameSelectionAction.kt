package com.team23.ui.gameSelection

sealed interface GameSelectionAction {
    data class StartSolo(val forceCreate: Boolean) : GameSelectionAction
    data object CreateMulti : GameSelectionAction
    data class JoinMulti(val rawGameId: String) : GameSelectionAction
}
