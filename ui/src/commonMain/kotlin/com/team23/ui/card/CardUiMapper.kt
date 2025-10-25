package com.team23.ui.card

import com.team23.domain.model.Card
import com.team23.ui.shape.FillingTypeUiModel

class CardUiMapper {

    fun toUiModel(
        card: Card,
        isSelected: Boolean,
        isPortrait: Boolean,
    ): CardUiModel = CardUiModel(
        patternAmount = card.number,
        color = toUiColor(card.color),
        fillingType = toUiFilling(card.fill),
        shape = toUiShape(card.shape),
        selected = isSelected,
        isPortraitMode = isPortrait,
    )

    private fun toUiColor(color: Card.Color): CardUiModel.Color = when (color) {
        Card.Color.PRIMARY -> CardUiModel.Color.Primary
        Card.Color.SECONDARY -> CardUiModel.Color.Secondary
        Card.Color.TERTIARY -> CardUiModel.Color.Tertiary
    }

    private fun toUiFilling(filling: Card.Fill): FillingTypeUiModel = when (filling) {
        Card.Fill.SOLID -> FillingTypeUiModel.Filled
        Card.Fill.STRIPED -> FillingTypeUiModel.Striped
        Card.Fill.EMPTY -> FillingTypeUiModel.Outlined
    }

    private fun toUiShape(shape: Card.Shape): CardUiModel.Shape = when (shape) {
        Card.Shape.OVAL -> CardUiModel.Shape.Oval
        Card.Shape.DIAMOND -> CardUiModel.Shape.Diamond
        Card.Shape.SQUIGGLE -> CardUiModel.Shape.Squiggle
    }

    fun toDomainModel(card: CardUiModel): Card = Card(
        color = toDomainColor(card.color),
        shape = toDomainShape(card.shape),
        number = card.patternAmount,
        fill = toDomainFilling(card.fillingType),
    )

    private fun toDomainColor(color: CardUiModel.Color): Card.Color = when (color) {
        CardUiModel.Color.Primary -> Card.Color.PRIMARY
        CardUiModel.Color.Secondary -> Card.Color.SECONDARY
        CardUiModel.Color.Tertiary -> Card.Color.TERTIARY
    }

    private fun toDomainFilling(filling: FillingTypeUiModel): Card.Fill = when (filling) {
        FillingTypeUiModel.Filled -> Card.Fill.SOLID
        FillingTypeUiModel.Striped -> Card.Fill.STRIPED
        FillingTypeUiModel.Outlined -> Card.Fill.EMPTY
    }

    private fun toDomainShape(shape: CardUiModel.Shape): Card.Shape = when (shape) {
        CardUiModel.Shape.Oval -> Card.Shape.OVAL
        CardUiModel.Shape.Diamond -> Card.Shape.DIAMOND
        CardUiModel.Shape.Squiggle -> Card.Shape.SQUIGGLE
    }
}
