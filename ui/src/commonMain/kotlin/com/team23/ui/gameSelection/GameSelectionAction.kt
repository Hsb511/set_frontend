package com.team23.ui.gameSelection

sealed interface GameSelectionAction {
    data class CreateSolo(val hasAnOngoingSoloGame: Boolean) : GameSelectionAction
    data object ContinueSolo : GameSelectionAction
    data object StartMulti : GameSelectionAction
}
