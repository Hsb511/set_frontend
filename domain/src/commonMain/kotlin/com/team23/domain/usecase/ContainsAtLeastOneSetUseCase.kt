package com.team23.domain.usecase

import com.team23.domain.model.Card

class ContainsAtLeastOneSetUseCase(
    private val isSetUseCase: IsSetUseCase,
) {

    fun invoke(cards: List<Card>): Boolean {
        for (card1 in cards) {
            for (card2 in cards) {
                for (card3 in cards) {
                    if (isSetUseCase.invoke(setOf(card1, card2, card3))) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
