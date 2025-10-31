package com.team23.data.game

import com.team23.domain.game.model.Card
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.statemachine.GameState

class GameRepositoryImpl: GameRepository {
    override suspend fun createSoloGame(): Result<GameState.Playing> {
        return Result.success(initializeGame())
    }

    private fun initializeGame(): GameState.Playing {
        val fullDeck = createFullDeck().shuffled()
        val table = fullDeck.take(12)
        val remainingDeck = fullDeck.drop(12)
        return GameState.Playing(deck = remainingDeck, table = table)
    }

    private fun createFullDeck(): List<Card> {
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
        }
    }
}
