package com.team23.domain.game.usecase

import com.team23.domain.game.GameTestUtils.createCard
import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateNewSoloGameUseCaseTest {

    private lateinit var isSetUseCase: IsSetUseCase
    private lateinit var containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase
    private lateinit var createFullShuffledDeckUseCase: CreateFullShuffledDeckUseCase
    private lateinit var useCase: CreateNewSoloGameUseCase

    @BeforeTest
    fun setup() {
        isSetUseCase = IsSetUseCase()
        containsAtLeastOneSetUseCase = ContainsAtLeastOneSetUseCase(isSetUseCase)
        createFullShuffledDeckUseCase = mock<CreateFullShuffledDeckUseCase>()
        useCase = CreateNewSoloGameUseCase(containsAtLeastOneSetUseCase, createFullShuffledDeckUseCase)
    }

    @Test
    fun `Given full shuffled deck has on top 12 cards with a set, When invoking use case, Then returns playing game with 12 card table and remaining deck`() {
        // Given
        every { createFullShuffledDeckUseCase.invoke() } returns create12CardsDeckWithSet()

        // When
        val gameState = useCase.invoke()

        // Then
        assertEquals(12,gameState.table.size)
        assertEquals(0,gameState.deck.size)
        assertEquals(0,gameState.selected.size)
    }

    @Test
    fun `Given full shuffled deck has on top 15 cards with a set, When invoking use case, Then returns playing game with 15 card table and remaining deck`() {
        // Given
        every { createFullShuffledDeckUseCase.invoke() } returns create15CardsDeckWithSet()

        // When
        val gameState = useCase.invoke()

        // Then
        assertEquals(15,gameState.table.size)
        assertEquals(0,gameState.deck.size)
        assertEquals(0,gameState.selected.size)
    }

    private fun create12CardsDeckWithSet() : List<Card.Data> = listOf(
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

    private fun create15CardsDeckWithSet() : List<Card.Data> = listOf(
        // Row 1
        createCard(1, Color.PRIMARY,   Shape.OVAL,     Fill.SOLID),   // 1OFR
        createCard(2, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 2OSR
        createCard(1, Color.TERTIARY,  Shape.OVAL,     Fill.SOLID),   // 1OFP

        // Row 2
        createCard(3, Color.PRIMARY,   Shape.OVAL,     Fill.STRIPED), // 3OSR
        createCard(1, Color.PRIMARY,   Shape.SQUIGGLE, Fill.EMPTY),   // 1SER
        createCard(3, Color.SECONDARY, Shape.OVAL,     Fill.STRIPED), // 3OSG

        // Row 3
        createCard(2, Color.TERTIARY,  Shape.DIAMOND,  Fill.EMPTY),   // 2DEP
        createCard(2, Color.TERTIARY,  Shape.SQUIGGLE, Fill.SOLID),   // 2SFP
        createCard(2, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),   // 2SEG

        // Row 4
        createCard(1, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),   // 1SEP
        createCard(2, Color.PRIMARY,   Shape.SQUIGGLE, Fill.SOLID),   // 2SFR
        createCard(3, Color.PRIMARY,   Shape.SQUIGGLE, Fill.SOLID),   // 3SFR

        // Row 5 with a set
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.SOLID),
        createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.EMPTY),
    )
}
