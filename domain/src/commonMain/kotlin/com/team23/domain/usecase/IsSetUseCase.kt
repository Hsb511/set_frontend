package com.team23.domain.usecase

import com.team23.domain.model.Card

class IsSetUseCase {

    fun invoke(cards: Set<Card>): Boolean {
        if (cards.filterIsInstance<Card.Data>().size != 3) {
            return false
        }

        val dataCards = cards.map { it as Card.Data }

        fun <T> List<T>.allSameOrAllDifferent() = toSet().size in setOf(1, 3)

        val sameNumberOr3DifferentNumbers   = dataCards.map { it.number }.allSameOrAllDifferent()
        val sameColorOr3DifferentColors     = dataCards.map { it.color }.allSameOrAllDifferent()
        val sameShapeOr3DifferentShapes     = dataCards.map { it.shape }.allSameOrAllDifferent()
        val sameFillingOr3DifferentFillings = dataCards.map { it.fill }.allSameOrAllDifferent()

        return sameNumberOr3DifferentNumbers &&
            sameColorOr3DifferentColors &&
            sameShapeOr3DifferentShapes &&
            sameFillingOr3DifferentFillings
    }
}
