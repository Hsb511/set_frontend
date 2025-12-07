package com.team23.ui.game

import com.team23.ui.card.Slot

sealed interface GameAction {
    data object StartSolo : GameAction
    data object StartMulti : GameAction
    data class SelectOrUnselectCard(val card: Slot) : GameAction
    data object Restart : GameAction
    data object RetryConfirmation : GameAction
    data object ChangeGameType : GameAction
    data object SelectSet: GameAction
}
