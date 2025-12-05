package com.team23.ui.game

import com.team23.domain.game.model.Card
import com.team23.domain.game.statemachine.GameState
import com.team23.ui.card.CardUiMapper
import com.team23.ui.card.Slot

class GameUiMapper(
    private val cardUiMapper: CardUiMapper,
) {

    fun toUiModel(
        gameState: GameState,
        isPortrait: Boolean,
        hasAnimation: Boolean,
    ): GameUiModel = when (gameState) {
        is GameState.EmptyDeck -> GameUiModel(
            isPortrait = isPortrait,
            hasAnimation = hasAnimation,
        )
        is GameState.Playing -> GameUiModel(
            cardsInDeck = mapCards(gameState.deck, gameState.selected, isPortrait),
            playingCards = mapCards(gameState.table, gameState.selected, isPortrait),
            isPortrait = isPortrait,
            hasAnimation = hasAnimation,
        )

        is GameState.Finished -> GameUiModel(
            playingCards = mapCards(gameState.cards, emptySet(), isPortrait),
            isPortrait = isPortrait,
            isFinished = true,
            hasAnimation = hasAnimation,
        )
    }

    private fun mapCards(
        cards: List<Card>,
        selected: Set<Card>,
        isPortrait: Boolean,
    ): List<Slot> = cards.mapIndexed { index, card ->
        cardUiMapper.toUiModel(
            card = card,
            isSelected = card in selected,
            isPortrait = isPortrait,
            index = index,
        )
    }
}
