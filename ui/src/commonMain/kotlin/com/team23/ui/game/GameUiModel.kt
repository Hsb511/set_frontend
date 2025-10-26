package com.team23.ui.game

import com.team23.ui.card.Slot

data class GameUiModel(
    val cardsInDeck: List<Slot> = emptyList(),
    val playingCards: List<Slot> = emptyList(),
    val selectedCards: Set<Slot> = emptySet(),
    val isPortrait: Boolean = true,
)
