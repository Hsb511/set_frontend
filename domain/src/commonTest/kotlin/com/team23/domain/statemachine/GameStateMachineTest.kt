package com.team23.domain.statemachine

import com.team23.domain.model.Card
import com.team23.domain.model.Card.Data.Color
import com.team23.domain.model.Card.Data.Fill
import com.team23.domain.model.Card.Data.Shape
import com.team23.domain.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.usecase.IsSetUseCase
import com.team23.domain.usecase.UpdateGameAfterSetFoundUseCase
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GameStateMachineTest {

    private lateinit var machine: GameStateMachine
    private lateinit var isSetUseCase: IsSetUseCase
    private lateinit var containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase
    private lateinit var updateGameAfterSetFoundUseCase: UpdateGameAfterSetFoundUseCase
    private lateinit var coroutineScope: CoroutineScope

    @BeforeTest
    fun setup() {
        isSetUseCase = IsSetUseCase()
        containsAtLeastOneSetUseCase = ContainsAtLeastOneSetUseCase(isSetUseCase)
        updateGameAfterSetFoundUseCase = UpdateGameAfterSetFoundUseCase(containsAtLeastOneSetUseCase)
        coroutineScope = CoroutineScope(EmptyCoroutineContext)
        machine = GameStateMachine(isSetUseCase, updateGameAfterSetFoundUseCase, coroutineScope)
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
        val fullDeck = createFullDeck()
        val table = fullDeck.take(12)
        val deck = fullDeck.drop(12)
        val initialState = GameState.Playing(deck = deck, table = table)

        val selected = table.take(3)

        // When
        val newState = machine.reduce(initialState, GameEvent.CardsSelected(selected.toSet()))

        // Then
        assertIs<GameState.Playing>(newState)

        assertEquals(66, newState.deck.size)
        assertEquals(12, newState.table.size)
        assertEquals(0, newState.selected.size)
    }

    @Test
    fun `Given a 12 to 15 cards table configuration, When selecting the set, Then the table contains 15 cards`() {
        // Given
        val table = createTableFor12to15Cards()
        val deck = createDeckFor12to15Cards()
        val initialState = GameState.Playing(deck = deck, table = table)

        val selected = table.take(3)

        // When
        val newState = machine.reduce(initialState, GameEvent.CardsSelected(selected.toSet()))

        // Then
        assertIs<GameState.Playing>(newState)
        assertEquals(15, newState.table.size)
        assertEquals(0, newState.deck.size)
    }

    @Test
    fun `Given a 15 to 15 cards table configuration, When selecting the set, Then the table contains 15 cards`() {
        // Given
        val table = createTableFor15to15Cards()
        val deck = createDeckFor15to15Cards()
        val initialState = GameState.Playing(deck = deck, table = table)

        val selected = table.take(3)

        // When
        val newState = machine.reduce(initialState, GameEvent.CardsSelected(selected.toSet()))

        // Then
        assertIs<GameState.Playing>(newState)
        assertEquals(15, newState.table.size)
        assertEquals(0, newState.deck.size)
    }

    @Test
    fun `Given a 15 to 12 cards table configuration, When selecting the set, Then the table has 12 cards correctly compacted`() {
        // Given
        val table = createTableFor15to12Cards()
        val deck = createDeckFor15to12Cards()
        val initialState = GameState.Playing(deck = deck, table = table)

        val selected = table.take(3)

        // When
        val newState = machine.reduce(initialState, GameEvent.CardsSelected(selected.toSet()))

        // Then
        assertIs<GameState.Playing>(newState)
        assertEquals(12, newState.table.size)
        assertEquals(3, newState.deck.size)
        val card1 = Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 1, fill = Fill.SOLID)
        val card2 = Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 2, fill = Fill.STRIPED)
        val card3 = Card.Data(color = Color.TERTIARY,  shape = Shape.OVAL,     number = 1, fill = Fill.SOLID)
        assertEquals(newState.table[0], card1)
        assertEquals(newState.table[1], card2)
        assertEquals(newState.table[2], card3)
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
        val finished = GameState.Finished(emptyList())

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

    private fun createTableFor12to15Cards() : List<Card> = listOf(
        // Row 1 with a set
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.STRIPED),
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.SOLID),
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.EMPTY),

        // Row 2
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 1, fill = Fill.SOLID),   // 1OFR
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 2, fill = Fill.STRIPED), // 2OSR
        Card.Data(color = Color.TERTIARY,  shape = Shape.OVAL,     number = 1, fill = Fill.SOLID),   // 1OFP

        // Row 3
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 3, fill = Fill.STRIPED), // 3OSR
        Card.Data(color = Color.PRIMARY,   shape = Shape.SQUIGGLE, number = 1, fill = Fill.EMPTY),   // 1SER
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL,     number = 3, fill = Fill.STRIPED), // 3OSG

        // Row 4
        Card.Data(color = Color.TERTIARY,  shape = Shape.DIAMOND,  number = 2, fill = Fill.EMPTY),   // 2DEP
        Card.Data(color = Color.TERTIARY,  shape = Shape.SQUIGGLE, number = 2, fill = Fill.SOLID),   // 2SFP
        Card.Data(color = Color.SECONDARY, shape = Shape.SQUIGGLE, number = 2, fill = Fill.EMPTY),   // 2SEG
    )

    private fun createDeckFor12to15Cards(): List<Card> = listOf(
        // Still no set here if you add them to Row 2, 3 and 4
        Card.Data(color = Color.SECONDARY, shape = Shape.SQUIGGLE, number = 1, fill = Fill.EMPTY),   // 1SEP
        Card.Data(color = Color.PRIMARY,   shape = Shape.SQUIGGLE, number = 2, fill = Fill.SOLID),   // 2SFR
        Card.Data(color = Color.PRIMARY,   shape = Shape.SQUIGGLE, number = 3, fill = Fill.SOLID),   // 3SFR

        // This is a set
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 1, fill = Fill.STRIPED),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 2, fill = Fill.SOLID),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 3, fill = Fill.EMPTY),
    )
    private fun createTableFor15to12Cards() : List<Card> = listOf(
        // Row 1 with a set
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.STRIPED),
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.SOLID),
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.EMPTY),

        // Row 2 with another set
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 1, fill = Fill.STRIPED),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 2, fill = Fill.SOLID),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 3, fill = Fill.EMPTY),

        // Row 3
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 3, fill = Fill.STRIPED), // 3OSR
        Card.Data(color = Color.PRIMARY,   shape = Shape.SQUIGGLE, number = 1, fill = Fill.EMPTY),   // 1SER
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL,     number = 3, fill = Fill.STRIPED), // 3OSG

        // Row 4
        Card.Data(color = Color.TERTIARY,  shape = Shape.DIAMOND,  number = 2, fill = Fill.EMPTY),   // 2DEP
        Card.Data(color = Color.TERTIARY,  shape = Shape.SQUIGGLE, number = 2, fill = Fill.SOLID),   // 2SFP
        Card.Data(color = Color.SECONDARY, shape = Shape.SQUIGGLE, number = 2, fill = Fill.EMPTY),   // 2SEG

        // Row 5
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 1, fill = Fill.SOLID),   // 1OFR
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 2, fill = Fill.STRIPED), // 2OSR
        Card.Data(color = Color.TERTIARY,  shape = Shape.OVAL,     number = 1, fill = Fill.SOLID),   // 1OFP
    )

    private fun createDeckFor15to12Cards(): List<Card> = listOf(
        // This is a set
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 1, fill = Fill.STRIPED),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 2, fill = Fill.SOLID),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 3, fill = Fill.EMPTY),
    )
    private fun createTableFor15to15Cards() : List<Card> = listOf(
        // Row 1 with a set
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.STRIPED),
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.SOLID),
        Card.Data(color = Color.SECONDARY, shape = Shape.DIAMOND, number = 1, fill = Fill.EMPTY),

        // Row 2
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 1, fill = Fill.SOLID),   // 1OFR
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 2, fill = Fill.STRIPED), // 2OSR
        Card.Data(color = Color.TERTIARY,  shape = Shape.OVAL,     number = 1, fill = Fill.SOLID),   // 1OFP

        // Row 3
        Card.Data(color = Color.PRIMARY,   shape = Shape.OVAL,     number = 3, fill = Fill.STRIPED), // 3OSR
        Card.Data(color = Color.PRIMARY,   shape = Shape.SQUIGGLE, number = 1, fill = Fill.EMPTY),   // 1SER
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL,     number = 3, fill = Fill.STRIPED), // 3OSG

        // Row 4
        Card.Data(color = Color.TERTIARY,  shape = Shape.DIAMOND,  number = 2, fill = Fill.EMPTY),   // 2DEP
        Card.Data(color = Color.TERTIARY,  shape = Shape.SQUIGGLE, number = 2, fill = Fill.SOLID),   // 2SFP
        Card.Data(color = Color.SECONDARY, shape = Shape.SQUIGGLE, number = 2, fill = Fill.EMPTY),   // 2SEG

        // Row 5
        Card.Data(color = Color.SECONDARY, shape = Shape.SQUIGGLE, number = 1, fill = Fill.EMPTY),   // 1SEP
        Card.Data(color = Color.PRIMARY,   shape = Shape.SQUIGGLE, number = 2, fill = Fill.SOLID),   // 2SFR
        Card.Data(color = Color.PRIMARY,   shape = Shape.SQUIGGLE, number = 3, fill = Fill.SOLID),   // 3SFR
    )

    private fun createDeckFor15to15Cards(): List<Card> = listOf(
        // This is a set
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 1, fill = Fill.STRIPED),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 2, fill = Fill.SOLID),
        Card.Data(color = Color.SECONDARY, shape = Shape.OVAL, number = 3, fill = Fill.EMPTY),
    )
}
