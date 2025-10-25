package com.team23.ui.game

import com.team23.ui.card.CardUiModel

data class GameUiModel(
    val cardsInDeck: List<CardUiModel> = emptyList(),
    val playingCards: List<CardUiModel> = emptyList(),
    val selectedCards: Set<CardUiModel> = emptySet(),
    val isPortrait: Boolean = true,
)
