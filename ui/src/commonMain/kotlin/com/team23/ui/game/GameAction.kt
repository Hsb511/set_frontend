package com.team23.ui.game

import com.team23.ui.card.Slot

sealed interface GameAction {
    data class SelectOrUnselectCard(val card: Slot) : GameAction
    data object Restart : GameAction
}
