package com.team23.ui.gametype

sealed interface GameTypeAction {
    data object StartSolo: GameTypeAction
    data object StartMulti: GameTypeAction
}
