package com.team23.domain.model

data class Card(
    val color: Color,
    val shape: Shape,
    val number: Int,
    val fill: Fill
) {
    enum class Color { PRIMARY, SECONDARY, TERTIARY }
    enum class Shape { OVAL, DIAMOND, SQUIGGLE }
    enum class Fill { SOLID, STRIPED, EMPTY }
}
