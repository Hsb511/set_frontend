package com.team23.domain.game.usecase

import com.team23.domain.game.model.Card

class FindFirstSetUseCase(
    private val isSetUseCase: IsSetUseCase,
) {

    fun invoke(cards: List<Card>): Set<Card.Data>? {
        val dataCards = cards.filterIsInstance<Card.Data>()
        for (card1 in dataCards) {
            for (card2 in dataCards) {
                for (card3 in dataCards) {
                    if (isSetUseCase.invoke(setOf(card1, card2, card3))) {
                        return setOf(card1, card2, card3)
                    }
                }
            }
        }
        return null
    }
}
