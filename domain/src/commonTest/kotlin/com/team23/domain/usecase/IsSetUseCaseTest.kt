package com.team23.domain.usecase

import com.team23.domain.model.Card
import com.team23.domain.model.Card.Data.Color
import com.team23.domain.model.Card.Data.Fill
import com.team23.domain.model.Card.Data.Shape
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsSetUseCaseTest {

    private lateinit var useCase: IsSetUseCase

    @BeforeTest
    fun setup() {
        useCase = IsSetUseCase()
    }

    @Test
    fun `Given three cards sharing three attributes and differing in one, When checking set, Then result is true`() {
        // Given
        val cards = setOf(
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(3, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
        )

        // When
        val result = useCase.invoke(cards)

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given three cards sharing two attributes and differing in two, When checking set, Then result is true`() {
        // Given
        val cards = setOf(
            createCard(1, Color.PRIMARY, Shape.DIAMOND, Fill.SOLID),
            createCard(2, Color.PRIMARY, Shape.DIAMOND, Fill.STRIPED),
            createCard(3, Color.PRIMARY, Shape.DIAMOND, Fill.EMPTY),
        )

        // When
        val result = useCase.invoke(cards)

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given three cards sharing one attribute and differing in three, When checking set, Then result is true`() {
        // Given
        val cards = setOf(
            createCard(1, Color.SECONDARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
            createCard(3, Color.SECONDARY, Shape.SQUIGGLE, Fill.EMPTY),
        )

        // When
        val result = useCase.invoke(cards)

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given three cards all differing on every attribute, When checking set, Then result is true`() {
        // Given
        val cards = setOf(
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
            createCard(3, Color.TERTIARY, Shape.SQUIGGLE, Fill.EMPTY),
        )

        // When
        val result = useCase.invoke(cards)

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given three Card Data with two same and one different on one attribute, When checking set, Then result is false`() {
        // Given
        val cards = setOf(
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(1, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
            createCard(2, Color.TERTIARY, Shape.SQUIGGLE, Fill.EMPTY),
        )

        // When
        val result = useCase.invoke(cards)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given only two Card Data, When checking set, Then result is false`() {
        // Given
        val cards = setOf(
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.SECONDARY, Shape.OVAL, Fill.SOLID),
        )

        // When
        val result = useCase.invoke(cards)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given two Card Data and one Empty card, When checking set, Then result is false`() {
        // Given
        val cards = setOf(
            createCard(1, Color.PRIMARY, Shape.OVAL, Fill.SOLID),
            createCard(2, Color.SECONDARY, Shape.DIAMOND, Fill.STRIPED),
            Card.Empty
        )

        // When
        val result = useCase.invoke(cards)

        // Then
        assertFalse(result)
    }

    private fun createCard(number: Int, color: Color, shape: Shape, fill: Fill) =
        Card.Data(color, shape, number, fill)
}
