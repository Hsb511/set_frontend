package com.team23.domain.game.usecase

import com.team23.domain.game.model.Card

class ContainsAtLeastOneSetUseCase(
    private val findFirstSetUseCase: FindFirstSetUseCase,
) {

    fun invoke(cards: List<Card>): Boolean = findFirstSetUseCase.invoke(cards) != null
}
