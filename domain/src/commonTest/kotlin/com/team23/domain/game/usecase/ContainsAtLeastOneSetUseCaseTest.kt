package com.team23.domain.game.usecase

import com.team23.domain.game.GameTestUtils.createCard
import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContainsAtLeastOneSetUseCaseTest {


    private lateinit var isSetUseCase: IsSetUseCase
    private lateinit var findFirstSetUseCase: FindFirstSetUseCase
    private lateinit var useCase: ContainsAtLeastOneSetUseCase

    @BeforeTest
    fun setup() {
        isSetUseCase = IsSetUseCase()
        findFirstSetUseCase = FindFirstSetUseCase(isSetUseCase)
        useCase = ContainsAtLeastOneSetUseCase(findFirstSetUseCase)
    }

    @Test
    fun `Given no cards, When checking if it contains at least one set, Then returns false`() {
        // Given
        val cards = emptyList<Card>()

        // When
        val containsAtLeastOneSet = useCase.invoke(cards)

        // Then
        assertFalse(containsAtLeastOneSet)
    }

    @Test
    fun `Given 3 cards which are not a set, When checking if it contains at least one set, Then returns false`() {
        // Given
        val cards = listOf(
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.SECONDARY, Shape.OVAL, Fill.SOLID),
        )

        // When
        val containsAtLeastOneSet = useCase.invoke(cards)

        // Then
        assertFalse(containsAtLeastOneSet)
    }

    @Test
    fun `Given 3 cards which are a set, When checking if it contains at least one set, Then returns true`() {
        // Given
        val cards = listOf(
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(3, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
        )

        // When
        val containsAtLeastOneSet = useCase.invoke(cards)

        // Then
        assertTrue(containsAtLeastOneSet)
    }

    @Test
    fun `Given 12 cards with 3 sets, When checking if it contains at least one set, Then returns true`() {
        // Given
        val cards = listOf(
            // --- Set 1: all same color, shape, fill; different numbers
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(3, Color.PRIMARY, Shape.OVAL, Fill.SOLID),

            // --- Set 2: same color & shape; different fills & numbers
            createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.SOLID),
            createCard(2, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
            createCard(3, Color.SECONDARY, Shape.DIAMOND, Fill.EMPTY),

            // --- Set 3: same color; all other attributes different
            createCard(1, Color.TERTIARY, Shape.OVAL, Fill.EMPTY),
            createCard(2, Color.TERTIARY, Shape.DIAMOND, Fill.STRIPED),
            createCard(3, Color.TERTIARY, Shape.SQUIGGLE, Fill.SOLID),

            // --- Three filler cards that do NOT form any set among themselves
            createCard(1, Color.PRIMARY, Shape.DIAMOND, Fill.STRIPED),
            createCard(2, Color.SECONDARY, Shape.SQUIGGLE, Fill.SOLID),
            createCard(3, Color.TERTIARY, Shape.OVAL, Fill.STRIPED),
        )

        // When
        val containsAtLeastOneSet = useCase.invoke(cards)

        // Then
        assertTrue(containsAtLeastOneSet)
    }
}
