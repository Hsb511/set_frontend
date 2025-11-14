package com.team23.domain.game.statemachine

import com.team23.domain.game.GameTestUtils.createCard
import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.game.usecase.CreateFullShuffledDeckUseCase
import com.team23.domain.game.usecase.CreateFullShuffledDeckUseCaseImpl
import com.team23.domain.game.usecase.CreateNewSoloGameUseCase
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import com.team23.domain.startup.model.GameType
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GameStateMachineTest {

    private lateinit var machine: GameStateMachine
    private lateinit var isSetUseCase: IsSetUseCase
    private lateinit var containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase
    private lateinit var updateGameAfterSetFoundUseCase: UpdateGameAfterSetFoundUseCase
    private lateinit var createFullShuffledDeckUseCase: CreateFullShuffledDeckUseCase
    private lateinit var createNewSoloGameUseCase: CreateNewSoloGameUseCase
    private lateinit var gameRepository: GameRepository

    @BeforeTest
    fun setup() {
        isSetUseCase = IsSetUseCase()
        containsAtLeastOneSetUseCase = ContainsAtLeastOneSetUseCase(isSetUseCase)
        createFullShuffledDeckUseCase = CreateFullShuffledDeckUseCaseImpl()
        createNewSoloGameUseCase = CreateNewSoloGameUseCase(containsAtLeastOneSetUseCase, createFullShuffledDeckUseCase)
        updateGameAfterSetFoundUseCase = UpdateGameAfterSetFoundUseCase(containsAtLeastOneSetUseCase)
        gameRepository = mock<GameRepository>()
        machine = GameStateMachine(
            createNewSoloGameUseCase,
            isSetUseCase,
            updateGameAfterSetFoundUseCase,
            gameRepository,
        )
    }

    @Test
    fun `when state is EmptyDeck and event is Init then state becomes Playing with 12 cards on table`() = runTest {
        // Given
        val initialState = GameState.EmptyDeck
        /*everySuspend { gameRepository.createSoloGame() } returns Result.success(GameState.Playing(
            deck = List(81) { Card.Empty },
            table = List(12) { Card.Empty },
        ))*/

        // When
        val newState = machine.reduce(initialState, GameEvent.Init(GameType.Solo))

        // Then
        assertIs<GameState.Playing>(newState)
    }

    @Test
    fun `when selecting 3 cards during play then 3 cards are removed and replaced`() = runTest {
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
    fun `Given a 12 to 15 cards table configuration, When selecting the set, Then the table contains 15 cards`() = runTest {
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
    fun `Given a 15 to 15 cards table configuration, When selecting the set, Then the table contains 15 cards`() = runTest {
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
    fun `Given a 15 to 12 cards table configuration, When selecting the set, Then the table has 12 cards correctly compacted`() = runTest {
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
    fun `when selecting less than 3 cards only selected cards changes`() = runTest {
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
    fun `when deck and table are empty after selection then state becomes Finished`() = runTest {
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
    fun `Init event on Finished or Playing state does nothing`() = runTest {
        // Given
        val playing = GameState.Playing(
            deck = emptyList(),
            table = emptyList()
        )
        val finished = GameState.Finished(emptyList())

        // When
        val result1 = machine.reduce(playing, GameEvent.Init(GameType.Solo))
        val result2 = machine.reduce(finished, GameEvent.Init(GameType.Solo))

        // Then
        assertEquals(playing, result1)
        assertEquals(finished, result2)
    }

    private fun createFullDeck(): List<Card.Data> {
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

    private fun createTableFor12to15Cards() : List<Card.Data> = listOf(
        // Row 1 with a set
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.SOLID),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.EMPTY),

        // Row 2
        createCard(1, Color.PRIMARY,   Shape.OVAL,     Fill.SOLID),   // 1OFR
        createCard(2, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 2OSR
        createCard(1, Color.TERTIARY,  Shape.OVAL,     Fill.SOLID),   // 1OFP

        // Row 3
        createCard(3, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 3OSR
        createCard(1, Color.PRIMARY,   Shape.SQUIGGLE, Fill.EMPTY),   // 1SER
        createCard(3, Color.SECONDARY, Shape.OVAL,     Fill.STRIPED), // 3OSG

        // Row 4
        createCard(2, Color.TERTIARY,  Shape.DIAMOND,  Fill.EMPTY),   // 2DEP
        createCard(2, Color.TERTIARY,  Shape.SQUIGGLE, Fill.SOLID),   // 2SFP
        createCard(2, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),   // 2SEG
    )

    private fun createDeckFor12to15Cards(): List<Card.Data> = listOf(
        // Still no set here if you add them to Row 2, 3 and 4
        createCard(1, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),   // 1SEP
        createCard(2, Color.PRIMARY,   Shape.SQUIGGLE, Fill.SOLID),   // 2SFR
        createCard(3, Color.PRIMARY,   Shape.SQUIGGLE, Fill.SOLID),   // 3SFR

        // This is a set
        createCard(1, Color.SECONDARY, Shape.OVAL, Fill.STRIPED),
        createCard(2, Color.SECONDARY, Shape.OVAL, Fill.SOLID),
        createCard(3, Color.SECONDARY, Shape.OVAL, Fill.EMPTY),
    )

    private fun createTableFor15to12Cards() : List<Card.Data> = listOf(
        // Row 1 with a set
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.SOLID),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.EMPTY),

        // Row 2 with another set
        createCard(1, Color.SECONDARY, Shape.OVAL, Fill.STRIPED),
        createCard(2, Color.SECONDARY, Shape.OVAL, Fill.SOLID),
        createCard(3, Color.SECONDARY, Shape.OVAL, Fill.EMPTY),

        // Row 3
        createCard(3, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 3OSR
        createCard(1, Color.PRIMARY,   Shape.SQUIGGLE, Fill.EMPTY),   // 1SER
        createCard(3, Color.SECONDARY, Shape.OVAL,     Fill.STRIPED), // 3OSG

        // Row 4
        createCard(2, Color.TERTIARY,  Shape.DIAMOND,  Fill.EMPTY),   // 2DEP
        createCard(2, Color.TERTIARY,  Shape.SQUIGGLE, Fill.SOLID),   // 2SFP
        createCard(2, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),   // 2SEG

        // Row 5
        createCard(1, Color.PRIMARY,   Shape.OVAL,     Fill.SOLID),   // 1OFR
        createCard(2, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 2OSR
        createCard(1, Color.TERTIARY,  Shape.OVAL,     Fill.SOLID),   // 1OFP
    )

    private fun createDeckFor15to12Cards(): List<Card.Data> = listOf(
        // This is a set
        createCard(1, Color.SECONDARY, Shape.OVAL, Fill.STRIPED),
        createCard(2, Color.SECONDARY, Shape.OVAL, Fill.SOLID),
        createCard(3, Color.SECONDARY, Shape.OVAL, Fill.EMPTY),
    )

    private fun createTableFor15to15Cards() : List<Card.Data> = listOf(
        // Row 1 with a set
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.SOLID),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.EMPTY),

        // Row 2
        createCard(1, Color.PRIMARY,   Shape.OVAL,     Fill.SOLID),   // 1OFR
        createCard(2, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 2OSR
        createCard(1, Color.TERTIARY,  Shape.OVAL,     Fill.SOLID),   // 1OFP

        // Row 3
        createCard(3, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 3OSR
        createCard(1, Color.PRIMARY,   Shape.SQUIGGLE, Fill.EMPTY),   // 1SER
        createCard(3, Color.SECONDARY, Shape.OVAL,     Fill.STRIPED), // 3OSG

        // Row 4
        createCard(2, Color.TERTIARY,  Shape.DIAMOND,  Fill.EMPTY),   // 2DEP
        createCard(2, Color.TERTIARY,  Shape.SQUIGGLE, Fill.SOLID),   // 2SFP
        createCard(2, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),   // 2SEG

        // Row 5
        createCard(1, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),   // 1SEP
        createCard(2, Color.PRIMARY,   Shape.SQUIGGLE, Fill.SOLID),   // 2SFR
        createCard(3, Color.PRIMARY,   Shape.SQUIGGLE, Fill.SOLID),   // 3SFR
    )

    private fun createDeckFor15to15Cards(): List<Card.Data> = listOf(
        // This is a set
        createCard(1, Color.SECONDARY, Shape.OVAL, Fill.STRIPED),
        createCard(2, Color.SECONDARY, Shape.OVAL, Fill.SOLID),
        createCard(3, Color.SECONDARY, Shape.OVAL, Fill.EMPTY),
    )
}
