package com.team23.domain.statemachine

import com.team23.domain.model.Card
import com.team23.domain.model.Card.Data.Color
import com.team23.domain.model.Card.Data.Fill
import com.team23.domain.model.Card.Data.Shape
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GameStateMachineTest {

    private lateinit var machine: GameStateMachine

    @BeforeTest
    fun setup() {
        machine = GameStateMachine()
    }

    @Test
    fun `when state is EmptyDeck and event is Init then state becomes Playing with 12 cards on table`() {
        // Given
        val initialState = GameState.EmptyDeck

        // When
        val newState = machine.reduce(initialState, GameEvent.Init)

        // Then
        assertIs<GameState.Playing>(newState)
        assertEquals(12, newState.table.size)
        assertEquals(81 - 12, newState.deck.size)
    }

    @Test
    fun `when selecting 3 cards during play then 3 cards are removed and replaced`() {
        // Given
        val fullDeck = createFullDeck().shuffled()
        val table = fullDeck.take(12)
        val deck = fullDeck.drop(12)
        val initialState = GameState.Playing(deck = deck, table = table)

        val selected = table.take(3)

        // When
        val newState = machine.reduce(initialState, GameEvent.CardsSelected(selected.toSet()))

        // Then
        assertIs<GameState.Playing>(newState)

        assertEquals(81 - 12 - 3, newState.deck.size) // 3 removed
        assertEquals(12, newState.table.size) // still 12 on table
        assertTrue(newState.table.none { it in selected })
    }

    @Test
    fun `when selecting less than 3 cards only selected cards changes`() {
        // Given
        val fullDeck = createFullDeck().shuffled()
        val table = fullDeck.take(12)
        val deck = fullDeck.drop(12)
        val initialState = GameState.Playing(deck = deck, table = table)

        val selected = table.take(2).toSet()

        // When
        val newState = machine.reduce(initialState, GameEvent.CardsSelected(selected))

        // Then
        assertIs<GameState.Playing>(newState)
        assertEquals(initialState.table, newState.table)
        assertEquals(initialState.deck, newState.deck)
        assertEquals(selected, newState.selected)
    }

    @Test
    fun `when deck and table are empty after selection then state becomes Finished`() {
        // Given
        val deck = emptyList<Card>()
        val table = listOf(
            Card.Data(Color.PRIMARY, Shape.OVAL, 1, Fill.SOLID),
            Card.Data(Color.SECONDARY, Shape.DIAMOND, 2, Fill.STRIPED),
            Card.Data(Color.TERTIARY, Shape.SQUIGGLE, 3, Fill.EMPTY)
        )
        val initialState = GameState.Playing(deck = deck, table = table)

        // When
        val newState = machine.reduce(initialState, GameEvent.CardsSelected(table.toSet()))

        // Then
        assertIs<GameState.Finished>(newState)
    }

    @Test
    fun `Init event on Finished or Playing state does nothing`() {
        // Given
        val playing = GameState.Playing(
            deck = emptyList(),
            table = emptyList()
        )
        val finished = GameState.Finished

        // When
        val result1 = machine.reduce(playing, GameEvent.Init)
        val result2 = machine.reduce(finished, GameEvent.Init)

        // Then
        assertEquals(playing, result1)
        assertEquals(finished, result2)
    }

    private fun createFullDeck(): List<Card> {
        val colors = Color.entries.toTypedArray()
        val shapes = Shape.entries.toTypedArray()
        val numbers = 1..3
        val fills = Fill.entries.toTypedArray()

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
