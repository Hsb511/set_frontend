package com.team23.domain.game.model

sealed interface Card {
    data object Empty: Card

    data class Data(
        val color: Color,
        val shape: Shape,
        val number: Int,
        val fill: Fill
    ): Card {
        enum class Color { PRIMARY, SECONDARY, TERTIARY }
        enum class Shape { OVAL, DIAMOND, SQUIGGLE }
        enum class Fill { SOLID, STRIPED, EMPTY }
    }
}
