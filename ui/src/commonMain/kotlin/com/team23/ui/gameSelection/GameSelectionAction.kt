package com.team23.ui.gameSelection

sealed interface GameSelectionAction {
    data class StartSolo(val forceCreate: Boolean) : GameSelectionAction
    data object CreateTimeTrial : GameSelectionAction
    data object CreateVersus : GameSelectionAction
    data class JoinMulti(val publicName: String) : GameSelectionAction
}
