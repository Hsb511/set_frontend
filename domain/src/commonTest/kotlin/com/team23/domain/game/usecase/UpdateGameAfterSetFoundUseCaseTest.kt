package com.team23.domain.game.usecase

import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape
import com.team23.domain.game.statemachine.GameState
import com.team23.domain.game.usecase.ContainsAtLeastOneSetUseCase
import com.team23.domain.game.usecase.IsSetUseCase
import com.team23.domain.game.usecase.UpdateGameAfterSetFoundUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertIs

class UpdateGameAfterSetFoundUseCaseTest {

    private lateinit var isSetUseCase: IsSetUseCase
    private lateinit var containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase
    private lateinit var useCase: UpdateGameAfterSetFoundUseCase

    @BeforeTest
    fun setup() {
        isSetUseCase = IsSetUseCase()
        containsAtLeastOneSetUseCase = ContainsAtLeastOneSetUseCase(isSetUseCase)
        useCase = UpdateGameAfterSetFoundUseCase(containsAtLeastOneSetUseCase)
    }

    @Test
    fun `input table with 12 cards, output table with 12 cards`() {
        // Given table with 12 cards and top 3 cards will create a set
        val table = row1 + set1 + row3 + row4
        val setFound = set1.toSet()
        val deck = set2

        // When updating the table after set has been found
        val gameState = useCase.invoke(table, setFound, deck)
        assertIs<GameState.Playing>(gameState)

        // Then replace the cards from the set by the top ones from the deck to have a 12 cards table
        assertContentEquals(row1 + set2 + row3 + row4, gameState.table)
        assertContentEquals(emptyList(), gameState.deck)
    }

    @Test
    fun `input table with 12 cards, output table with 15 cards`() {
        // Given table with 12 cards, top 3 cards will not create a set but the 3 next ones will
        val table = row1 + set1 + row3 + row4
        val setFound = set1.toSet()
        val deck = row2 + set2

        // When updating the table after set has been found
        val gameState = useCase.invoke(table, setFound, deck)
        assertIs<GameState.Playing>(gameState)

        // Then replace the cards from the set by the top 3 ones from the deck and append the 3 next ones to have a 15 cards table
        assertContentEquals(row1 + row2 + row3 + row4 + set2, gameState.table)
        assertContentEquals(emptyList(), gameState.deck)
    }

    @Test
    fun `input table with 15 cards, output table with 15 cards`() {
        // Given table with 15 cards and a single set and top 3 cards will create a set
        val table = row1 + set1 + row2 + row3 + row4
        val setFound = set1.toSet()
        val deck = set2

        // When updating the table after set has been found
        val gameState = useCase.invoke(table, setFound, deck)
        assertIs<GameState.Playing>(gameState)

        // Then replace the cards from the set by the top ones from the deck to have a 15 cards table
        assertContentEquals(row1 + set2 + row2 + row3 + row4, gameState.table)
        assertContentEquals(emptyList(), gameState.deck)
    }

    @Test
    fun `input table with 15 cards, output table with 12 cards`() {
        // Given table with 15 cards and two sets and the top 3 card of the deck
        val table = row1 + row2 + row3 + set1 + set2
        val setFound = set1.toSet()
        val deck = row4

        // When updating the table after set has been found
        val gameState = useCase.invoke(table, setFound, deck)
        assertIs<GameState.Playing>(gameState)

        // Then remove the cards of the set and move only the last 3 cards of the table to take the place of the cards removed
        assertContentEquals(row1 + row2 + row3 + set2, gameState.table)
        assertContentEquals(row4, gameState.deck)
    }

    @Test
    fun `input table with 12 cards, output table with 9 cards and 3 empty`() {
        // Given table with 12 cards and deck is empty
        val table = row1 + row2 + set1 + set2
        val setFound = set1.toSet()
        val deck = emptyList<Card>()

        // When updating the table after set has been found
        val gameState = useCase.invoke(table, setFound, deck)
        assertIs<GameState.Playing>(gameState)

        // Then replace the cards in the table by empty ones
        assertContentEquals(row1 + row2 + emptyRow + set2, gameState.table)
        assertContentEquals(emptyList(), gameState.deck)
    }

    @Test
    fun `input table with 9 cards, output table with 9 cards and game finished`() {
        // Given table with 9 cards and 3 empty and no set, deck is empty
        val table = row1 + set1 + row3 + emptyRow
        val setFound = set1.toSet()
        val deck = emptyList<Card>()

        // When updating the table after set has been found
        val gameState = useCase.invoke(table, setFound, deck)
        assertIs<GameState.Finished>(gameState)

        // Then replace the cards in the table by empty ones
        assertContentEquals(row1 + emptyRow + row3 + emptyRow, gameState.cards)
    }

    @Test
    fun `input table with 15 cards, output table with 12 cards and game finished`() {
        // Given table with 15 cards only 1 set, deck is empty
        val table = row1 + row2 + set1 + row3 + row4
        val setFound = set1.toSet()
        val deck = emptyList<Card>()

        // When updating the table after set has been found
        val gameState = useCase.invoke(table, setFound, deck)
        assertIs<GameState.Finished>(gameState)

        // Then replace the cards in the table by empty ones
        assertContentEquals(row1 + row2 + emptyRow + row3 + row4, gameState.cards)
    }

    private fun createCard(color: Color, shape: Shape, number: Int, fill: Fill) = Card.Data(color, shape, number, fill)

    // row1 + row2 + row3 + row4 contains no set
    private val row1 = listOf(
        createCard(Color.PRIMARY, Shape.OVAL, 2, Fill.EMPTY),
        createCard(Color.SECONDARY, Shape.OVAL, 3, Fill.STRIPED),
        createCard(Color.PRIMARY, Shape.SQUIGGLE, 1, Fill.EMPTY),
    )

    private val row2 = listOf(
        createCard(Color.PRIMARY, Shape.SQUIGGLE, 1, Fill.STRIPED),
        createCard(Color.TERTIARY, Shape.SQUIGGLE, 1, Fill.EMPTY),
        createCard(Color.SECONDARY, Shape.DIAMOND, 1, Fill.STRIPED),
    )

    private val row3 = listOf(
        createCard(Color.SECONDARY, Shape.SQUIGGLE, 1, Fill.STRIPED),
        createCard(Color.SECONDARY, Shape.SQUIGGLE, 2, Fill.EMPTY),
        createCard(Color.TERTIARY, Shape.DIAMOND, 3, Fill.STRIPED),
    )

    private val row4 = listOf(
        createCard(Color.PRIMARY, Shape.DIAMOND, 3, Fill.STRIPED),
        createCard(Color.TERTIARY, Shape.SQUIGGLE, 2, Fill.SOLID),
        createCard(Color.PRIMARY, Shape.DIAMOND, 2, Fill.EMPTY),
    )

    private val set1 = listOf(
        createCard(Color.PRIMARY, Shape.DIAMOND, 1, Fill.SOLID),
        createCard(Color.SECONDARY, Shape.DIAMOND, 1, Fill.SOLID),
        createCard(Color.TERTIARY, Shape.DIAMOND, 1, Fill.SOLID),
    )

    private val set2 = listOf(
        createCard(Color.PRIMARY, Shape.OVAL, 3, Fill.EMPTY),
        createCard(Color.SECONDARY, Shape.OVAL, 3, Fill.EMPTY),
        createCard(Color.TERTIARY, Shape.OVAL, 3, Fill.EMPTY),
    )

    private val emptyRow = List(3) { Card.Empty }
}
