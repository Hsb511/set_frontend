package com.team23.data.card

import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape
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
    fun `Given all the base 10 nominal raw codes, When mapping the code, Then returns correct`() {
        for (testCase in base10NominalTestCases) {
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
    fun `Given all the possible cards, When mapping it to base 10, Then returns correct code`() {
        for (testCase in base10NominalTestCases) {
            // Given
            val card = testCase.second

            // When
            val actualCode = mapper.toBase10Code(card)

            // Then
            val expectedCode = testCase.first
            assertEquals(expectedCode, actualCode)
        }
    }

    @Test
    fun `Given all the base 3 nominal raw codes, When mapping the code, Then returns correct`() {
        for (testCase in base3NominalTestCases) {
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
    fun `Given all the possible cards, When mapping it to base 3, Then returns correct code`() {
        for (testCase in base3NominalTestCases) {
            // Given
            val card = testCase.second

            // When
            val actualCode = mapper.toBase3Code(card)

            // Then
            val expectedCode = testCase.first
            assertEquals(expectedCode, actualCode)
        }
    }

    private val base10NominalTestCases = listOf(
        0 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.EMPTY),
        1 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.STRIPED),
        2 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.SOLID),
        3 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.EMPTY),
        4 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.STRIPED),
        5 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.SOLID),
        6 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.EMPTY),
        7 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.STRIPED),
        8 to Card.Data(number = 1, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.SOLID),
        9 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.EMPTY),
        10 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.STRIPED),
        11 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.SOLID),
        12 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.EMPTY),
        13 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.STRIPED),
        14 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.SOLID),
        15 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.EMPTY),
        16 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.STRIPED),
        17 to Card.Data(number = 1, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.SOLID),
        18 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.EMPTY),
        19 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.STRIPED),
        20 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.SOLID),
        21 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.EMPTY),
        22 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.STRIPED),
        23 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.SOLID),
        24 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.EMPTY),
        25 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.STRIPED),
        26 to Card.Data(number = 1, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.SOLID),
        27 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.EMPTY),
        28 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.STRIPED),
        29 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.SOLID),
        30 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.EMPTY),
        31 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.STRIPED),
        32 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.SOLID),
        33 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.EMPTY),
        34 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.STRIPED),
        35 to Card.Data(number = 2, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.SOLID),
        36 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.EMPTY),
        37 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.STRIPED),
        38 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.SOLID),
        39 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.EMPTY),
        40 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.STRIPED),
        41 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.SOLID),
        42 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.EMPTY),
        43 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.STRIPED),
        44 to Card.Data(number = 2, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.SOLID),
        45 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.EMPTY),
        46 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.STRIPED),
        47 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.SOLID),
        48 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.EMPTY),
        49 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.STRIPED),
        50 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.SOLID),
        51 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.EMPTY),
        52 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.STRIPED),
        53 to Card.Data(number = 2, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.SOLID),
        54 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.EMPTY),
        55 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.STRIPED),
        56 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.PRIMARY, fill = Fill.SOLID),
        57 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.EMPTY),
        58 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.STRIPED),
        59 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.SECONDARY, fill = Fill.SOLID),
        60 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.EMPTY),
        61 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.STRIPED),
        62 to Card.Data(number = 3, shape = Shape.DIAMOND, color = Color.TERTIARY, fill = Fill.SOLID),
        63 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.EMPTY),
        64 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.STRIPED),
        65 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.PRIMARY, fill = Fill.SOLID),
        66 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.EMPTY),
        67 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.STRIPED),
        68 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.SECONDARY, fill = Fill.SOLID),
        69 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.EMPTY),
        70 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.STRIPED),
        71 to Card.Data(number = 3, shape = Shape.SQUIGGLE, color = Color.TERTIARY, fill = Fill.SOLID),
        72 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.EMPTY),
        73 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.STRIPED),
        74 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.PRIMARY, fill = Fill.SOLID),
        75 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.EMPTY),
        76 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.STRIPED),
        77 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.SECONDARY, fill = Fill.SOLID),
        78 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.EMPTY),
        79 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.STRIPED),
        80 to Card.Data(number = 3, shape = Shape.OVAL, color = Color.TERTIARY, fill = Fill.SOLID),
    )

    private val base3NominalTestCases = listOf(
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
