package com.team23.data.card

import com.team23.domain.model.Card
import com.team23.domain.model.Card.Data.Color
import com.team23.domain.model.Card.Data.Fill
import com.team23.domain.model.Card.Data.Shape
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CardDataMapperTest {

    private lateinit var mapper: CardDataMapper

    @BeforeTest
    fun setup() {
        mapper = CardDataMapper()
    }

    @Test
    fun `Given all the nominal raw codes, When mapping the code, Then returns correct`() {
        for (testCase in nominalTestCases) {
            // Given
            val rawCode = testCase.first

            // When
            val actualCard = mapper.toDomainModel(rawCode)

            // Then
            val expectedCard = testCase.second
            assertEquals(expectedCard, actualCard)
        }
    }

    @Test
    fun `Given all the possible cards, When mapping it, Then returns correct code`() {
        for (testCase in nominalTestCases) {
            // Given
            val card = testCase.second

            // When
            val actualCode = mapper.toBase3Code(card)

            // Then
            val expectedCode = testCase.first
            assertEquals(expectedCode, actualCode)
        }
    }

    private val nominalTestCases = listOf(
        "0000" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.EMPTY),
        "0001" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.STRIPED),
        "0002" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.SOLID),
        "0010" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.EMPTY),
        "0011" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.STRIPED),
        "0012" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.SOLID),
        "0020" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.EMPTY),
        "0021" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.STRIPED),
        "0022" to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.SOLID),
        "0100" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.EMPTY),
        "0101" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.STRIPED),
        "0102" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.SOLID),
        "0110" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.EMPTY),
        "0111" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.STRIPED),
        "0112" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.SOLID),
        "0120" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.EMPTY),
        "0121" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.STRIPED),
        "0122" to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.SOLID),
        "0200" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.EMPTY),
        "0201" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.STRIPED),
        "0202" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.SOLID),
        "0210" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.EMPTY),
        "0211" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.STRIPED),
        "0212" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.SOLID),
        "0220" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.EMPTY),
        "0221" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.STRIPED),
        "0222" to Card.Data(number = 1, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.SOLID),
        "1000" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.EMPTY),
        "1001" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.STRIPED),
        "1002" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.SOLID),
        "1010" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.EMPTY),
        "1011" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.STRIPED),
        "1012" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.SOLID),
        "1020" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.EMPTY),
        "1021" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.STRIPED),
        "1022" to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.SOLID),
        "1100" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.EMPTY),
        "1101" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.STRIPED),
        "1102" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.SOLID),
        "1110" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.EMPTY),
        "1111" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.STRIPED),
        "1112" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.SOLID),
        "1120" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.EMPTY),
        "1121" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.STRIPED),
        "1122" to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.SOLID),
        "1200" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.EMPTY),
        "1201" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.STRIPED),
        "1202" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.SOLID),
        "1210" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.EMPTY),
        "1211" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.STRIPED),
        "1212" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.SOLID),
        "1220" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.EMPTY),
        "1221" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.STRIPED),
        "1222" to Card.Data(number = 2, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.SOLID),
        "2000" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.EMPTY),
        "2001" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.STRIPED),
        "2002" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.SOLID),
        "2010" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.EMPTY),
        "2011" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.STRIPED),
        "2012" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.SOLID),
        "2020" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.EMPTY),
        "2021" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.STRIPED),
        "2022" to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.SOLID),
        "2100" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.EMPTY),
        "2101" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.STRIPED),
        "2102" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.SOLID),
        "2110" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.EMPTY),
        "2111" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.STRIPED),
        "2112" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.SOLID),
        "2120" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.EMPTY),
        "2121" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.STRIPED),
        "2122" to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.SOLID),
        "2200" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.EMPTY),
        "2201" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.STRIPED),
        "2202" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.SOLID),
        "2210" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.EMPTY),
        "2211" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.STRIPED),
        "2212" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.SOLID),
        "2220" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.EMPTY),
        "2221" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.STRIPED),
        "2222" to Card.Data(number = 3, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.SOLID),
    )
}
