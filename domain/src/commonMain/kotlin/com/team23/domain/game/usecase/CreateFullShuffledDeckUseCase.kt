package com.team23.domain.game.usecase

import com.team23.domain.game.model.Card

interface CreateFullShuffledDeckUseCase {
    fun invoke(): List<Card>
}

class CreateFullShuffledDeckUseCaseImpl : CreateFullShuffledDeckUseCase {

    override fun invoke(): List<Card> {
        val colors = Card.Data.Color.entries.toTypedArray()
        val shapes = Card.Data.Shape.entries.toTypedArray()
        val numbers = 1..3
        val fills = Card.Data.Fill.entries.toTypedArray()

        return colors.flatMap { color ->
            shapes.flatMap { shape ->
                numbers.flatMap { number ->
                    fills.map { fill ->
                        Card.Data(color, shape, number, fill)
                    }
                }
            }
        }.shuffled()
    }
}
