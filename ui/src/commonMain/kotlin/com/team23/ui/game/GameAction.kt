package com.team23.ui.game

import com.team23.ui.card.CardUiModel

sealed interface GameAction {
    data class SelectOrUnselectCard(val card: CardUiModel) : GameAction
}
