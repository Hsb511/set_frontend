package com.team23.ui.game

import com.team23.ui.card.Slot

sealed interface GameUiEvent {
    data class AnimateSelectedCards(val cards: Set<Pair<Int, Slot.CardUiModel>>): GameUiEvent
}
