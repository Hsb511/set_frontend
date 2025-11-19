package com.team23.data.card

import com.team23.domain.game.model.Card

class CardDataMapper {

    fun toDomainModel(base10: SetCard): Card {
        if (base10 == -1) return Card.Empty
        require(base10 in 0..80) {
            "Invalid card '$base10': must be between 0 and 80"
        }
        return toDomainModel(base10.toString(radix = 3).padStart(4, '0'))
    }

    fun toDomainModel(base3: String): Card {
        require(base3_4.matches(base3)) {
            "Invalid card '$base3': expected 4 digits in base 3"
        }
        return Card.Data(
            number = base3[0].digitToInt() + 1,
            shape = toShape(base3[1].digitToInt()),
            color = toColor(base3[2].digitToInt()),
            fill = toFill(base3[3].digitToInt()),
        )
    }

    private fun toShape(digit: Int): Card.Data.Shape = when (digit) {
        0 -> Card.Data.Shape.DIAMOND
        1 -> Card.Data.Shape.SQUIGGLE
        else -> Card.Data.Shape.OVAL
    }

    private fun toColor(digit: Int): Card.Data.Color = when (digit) {
        0 -> Card.Data.Color.PRIMARY
        1 -> Card.Data.Color.SECONDARY
        else -> Card.Data.Color.TERTIARY
    }

    private fun toFill(digit: Int): Card.Data.Fill = when (digit) {
        0 -> Card.Data.Fill.EMPTY
        1 -> Card.Data.Fill.STRIPED
        else -> Card.Data.Fill.SOLID
    }

    fun toBase10Code(card: Card): SetCard = when (card) {
        is Card.Data -> toBase3Code(card).toInt(radix = 3)
        is Card.Empty -> -1
    }

    fun toBase3Code(card: Card.Data): String {
        val number = card.number - 1
        val shape = shapeToRawDigit(card.shape)
        val color = colorToRawDigit(card.color)
        val fill = fillToRawDigit(card.fill)

        return "$number$shape$color$fill"
    }

    private fun shapeToRawDigit(shape: Card.Data.Shape): Int = when (shape) {
        Card.Data.Shape.DIAMOND -> 0
        Card.Data.Shape.SQUIGGLE -> 1
        Card.Data.Shape.OVAL -> 2
    }

    private fun colorToRawDigit(color: Card.Data.Color): Int = when (color) {
        Card.Data.Color.PRIMARY -> 0
        Card.Data.Color.SECONDARY -> 1
        Card.Data.Color.TERTIARY -> 2
    }

    private fun fillToRawDigit(fill: Card.Data.Fill): Int = when (fill) {
        Card.Data.Fill.EMPTY -> 0
        Card.Data.Fill.STRIPED -> 1
        Card.Data.Fill.SOLID -> 2
    }

    companion object {
        private val base3_4 = Regex("""[012]{4}""")
    }
}
