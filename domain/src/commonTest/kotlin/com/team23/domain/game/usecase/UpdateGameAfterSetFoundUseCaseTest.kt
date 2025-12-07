package com.team23.domain.game.usecase

import com.team23.domain.game.GameTestUtils.createCard
import com.team23.domain.game.GameTestUtils.createPlayingGame
import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape
import com.team23.domain.game.statemachine.GameState
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertIs
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class UpdateGameAfterSetFoundUseCaseTest {

    private lateinit var isSetUseCase: IsSetUseCase
    private lateinit var findFirstSetUseCase: FindFirstSetUseCase
    private lateinit var containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase
    private lateinit var useCase: UpdateGameAfterSetFoundUseCase

    @BeforeTest
    fun setup() {
        isSetUseCase = IsSetUseCase()
        findFirstSetUseCase = FindFirstSetUseCase(isSetUseCase)
        containsAtLeastOneSetUseCase = ContainsAtLeastOneSetUseCase(findFirstSetUseCase)
        useCase = UpdateGameAfterSetFoundUseCase(containsAtLeastOneSetUseCase)
    }

    @Test
    fun `input table with 12 cards, output table with 12 cards`() {
        // Given table with 12 cards and top 3 cards will create a set
        val table = row1 + set1 + row3 + row4
        val setFound = set1.toSet()
        val deck = set2
        val game = createPlayingGame(table, deck)

        // When updating the table after set has been found
        val gameState = useCase.invoke(game, setFound)
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
        val game = createPlayingGame(table, deck)

        // When updating the table after set has been found
        val gameState = useCase.invoke(game, setFound)
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
        val game = createPlayingGame(table, deck)

        // When updating the table after set has been found
        val gameState = useCase.invoke(game, setFound)
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
        val game = createPlayingGame(table, deck)

        // When updating the table after set has been found
        val gameState = useCase.invoke(game, setFound)
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
        val deck = emptyList<Card.Data>()
        val game = createPlayingGame(table, deck)

        // When updating the table after set has been found
        val gameState = useCase.invoke(game, setFound)
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
        val deck = emptyList<Card.Data>()
        val game = createPlayingGame(table, deck)

        // When updating the table after set has been found
        val gameState = useCase.invoke(game, setFound)
        assertIs<GameState.Finished>(gameState)

        // Then replace the cards in the table by empty ones
        assertContentEquals(row1 + emptyRow + row3 + emptyRow, gameState.cards)
    }

    @Test
    fun `input table with 15 cards, output table with 12 cards and game finished`() {
        // Given table with 15 cards only 1 set, deck is empty
        val table = row1 + row2 + set1 + row3 + row4
        val setFound = set1.toSet()
        val deck = emptyList<Card.Data>()
        val game = createPlayingGame(table, deck)

        // When updating the table after set has been found
        val gameState = useCase.invoke(game, setFound)
        assertIs<GameState.Finished>(gameState)

        // Then replace the cards in the table by empty ones
        assertContentEquals(row1 + row2 + emptyRow + row3 + row4, gameState.cards)
    }

    // row1 + row2 + row3 + row4 contains no set
    private val row1 = listOf(
        createCard(2, Color.PRIMARY, Shape.OVAL, Fill.EMPTY),
        createCard(3, Color.SECONDARY, Shape.OVAL, Fill.STRIPED),
        createCard(1, Color.PRIMARY, Shape.SQUIGGLE, Fill.EMPTY),
    )

    private val row2 = listOf(
        createCard(1, Color.PRIMARY, Shape.SQUIGGLE, Fill.STRIPED),
        createCard(1, Color.TERTIARY, Shape.SQUIGGLE, Fill.EMPTY),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
    )

    private val row3 = listOf(
        createCard(1, Color.SECONDARY, Shape.SQUIGGLE, Fill.STRIPED),
        createCard(2, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),
        createCard(3, Color.TERTIARY, Shape.DIAMOND, Fill.STRIPED),
    )

    private val row4 = listOf(
        createCard(3, Color.PRIMARY, Shape.DIAMOND, Fill.STRIPED),
        createCard(2, Color.TERTIARY, Shape.SQUIGGLE, Fill.SOLID),
        createCard(2, Color.PRIMARY, Shape.DIAMOND, Fill.EMPTY),
    )

    private val set1 = listOf(
        createCard(1, Color.PRIMARY, Shape.DIAMOND, Fill.SOLID),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.SOLID),
        createCard(1, Color.TERTIARY, Shape.DIAMOND, Fill.SOLID),
    )

    private val set2 = listOf(
        createCard(3, Color.PRIMARY, Shape.OVAL, Fill.EMPTY),
        createCard(3, Color.SECONDARY, Shape.OVAL, Fill.EMPTY),
        createCard(3, Color.TERTIARY, Shape.OVAL, Fill.EMPTY),
    )

    private val emptyRow = List(3) { Card.Empty }
}
