package com.team23.ui.card

import com.team23.domain.model.Card
import com.team23.ui.shape.FillingTypeUiModel

class CardUiMapper {

    fun toUiModel(
        card: Card,
        isSelected: Boolean,
        isPortrait: Boolean,
    ): CardUiModel = when (card) {
        is Card.Data -> toDataUiModel(card, isSelected, isPortrait)
        is Card.Empty -> CardUiModel.Empty(isPortraitMode = isPortrait)
    }

    private fun toDataUiModel(card: Card.Data, isSelected: Boolean, isPortrait: Boolean) =
        CardUiModel.Data(
            patternAmount = card.number,
            color = toUiColor(card.color),
            fillingType = toUiFilling(card.fill),
            shape = toUiShape(card.shape),
            selected = isSelected,
            isPortraitMode = isPortrait,
        )

    private fun toUiColor(color: Card.Data.Color): CardUiModel.Color = when (color) {
        Card.Data.Color.PRIMARY -> CardUiModel.Color.Primary
        Card.Data.Color.SECONDARY -> CardUiModel.Color.Secondary
        Card.Data.Color.TERTIARY -> CardUiModel.Color.Tertiary
    }

    private fun toUiFilling(filling: Card.Data.Fill): FillingTypeUiModel = when (filling) {
        Card.Data.Fill.SOLID -> FillingTypeUiModel.Filled
        Card.Data.Fill.STRIPED -> FillingTypeUiModel.Striped
        Card.Data.Fill.EMPTY -> FillingTypeUiModel.Outlined
    }

    private fun toUiShape(shape: Card.Data.Shape): CardUiModel.Shape = when (shape) {
        Card.Data.Shape.OVAL -> CardUiModel.Shape.Oval
        Card.Data.Shape.DIAMOND -> CardUiModel.Shape.Diamond
        Card.Data.Shape.SQUIGGLE -> CardUiModel.Shape.Squiggle
    }

    fun toDomainModel(card: CardUiModel): Card = when(card) {
        is CardUiModel.Data -> toDataDomainModel(card)
        is CardUiModel.Empty -> Card.Empty
    }

    private fun toDataDomainModel(card: CardUiModel.Data) = Card.Data(
        color = toDomainColor(card.color),
        shape = toDomainShape(card.shape),
        number = card.patternAmount,
        fill = toDomainFilling(card.fillingType),
    )

    private fun toDomainColor(color: CardUiModel.Color): Card.Data.Color = when (color) {
        CardUiModel.Color.Primary -> Card.Data.Color.PRIMARY
        CardUiModel.Color.Secondary -> Card.Data.Color.SECONDARY
        CardUiModel.Color.Tertiary -> Card.Data.Color.TERTIARY
    }

    private fun toDomainFilling(filling: FillingTypeUiModel): Card.Data.Fill = when (filling) {
        FillingTypeUiModel.Filled -> Card.Data.Fill.SOLID
        FillingTypeUiModel.Striped -> Card.Data.Fill.STRIPED
        FillingTypeUiModel.Outlined -> Card.Data.Fill.EMPTY
    }

    private fun toDomainShape(shape: CardUiModel.Shape): Card.Data.Shape = when (shape) {
        CardUiModel.Shape.Oval -> Card.Data.Shape.OVAL
        CardUiModel.Shape.Diamond -> Card.Data.Shape.DIAMOND
        CardUiModel.Shape.Squiggle -> Card.Data.Shape.SQUIGGLE
    }
}
