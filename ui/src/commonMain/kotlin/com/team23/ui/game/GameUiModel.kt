package com.team23.ui.game

import com.team23.ui.card.Slot

data class GameUiModel(
    val cardsInDeck: List<Slot> = emptyList(),
    val playingCards: List<Slot> = emptyList(),
    val selectedCards: Set<Slot> = emptySet(),
    val isPortrait: Boolean = true,
    val isFinished: Boolean = false,
    val hasAnimation: Boolean = true,
    val timer: String = "00:00"
) {
    val isLoading: Boolean
        get() = cardsInDeck.isEmpty() && playingCards.isEmpty() && selectedCards.isEmpty() && !isFinished
}
