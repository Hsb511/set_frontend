package com.team23.ui.card

import com.team23.domain.game.model.Card
import com.team23.ui.shape.FillingTypeUiModel

class CardUiMapper {

    fun toUiModel(
        card: Card,
        isSelected: Boolean,
        isPortrait: Boolean,
        index: Int,
    ): Slot = when (card) {
        is Card.Data -> toUiModel(card, isSelected, isPortrait)
        is Card.Empty -> Slot.HoleUiModel(
            id = 10_000 * index,
            isPortraitMode = isPortrait,
        )
    }

    fun toUiModel(card: Card.Data, isSelected: Boolean, isPortrait: Boolean) =
        Slot.CardUiModel(
            patternAmount = card.number,
            color = toUiColor(card.color),
            fillingType = toUiFilling(card.fill),
            shape = toUiShape(card.shape),
            selected = isSelected,
            isPortraitMode = isPortrait,
        )

    private fun toUiColor(color: Card.Data.Color): Slot.CardUiModel.Color = when (color) {
        Card.Data.Color.PRIMARY -> Slot.CardUiModel.Color.Primary
        Card.Data.Color.SECONDARY -> Slot.CardUiModel.Color.Secondary
        Card.Data.Color.TERTIARY -> Slot.CardUiModel.Color.Tertiary
    }

    private fun toUiFilling(filling: Card.Data.Fill): FillingTypeUiModel = when (filling) {
        Card.Data.Fill.SOLID -> FillingTypeUiModel.Filled
        Card.Data.Fill.STRIPED -> FillingTypeUiModel.Striped
        Card.Data.Fill.EMPTY -> FillingTypeUiModel.Outlined
    }

    private fun toUiShape(shape: Card.Data.Shape): Slot.CardUiModel.Shape = when (shape) {
        Card.Data.Shape.OVAL -> Slot.CardUiModel.Shape.Oval
        Card.Data.Shape.DIAMOND -> Slot.CardUiModel.Shape.Diamond
        Card.Data.Shape.SQUIGGLE -> Slot.CardUiModel.Shape.Squiggle
    }

    fun toDomainModel(card: Slot): Card = when(card) {
        is Slot.CardUiModel -> toDataDomainModel(card)
        is Slot.HoleUiModel -> Card.Empty
    }

    private fun toDataDomainModel(card: Slot.CardUiModel) = Card.Data(
        color = toDomainColor(card.color),
        shape = toDomainShape(card.shape),
        number = card.patternAmount,
        fill = toDomainFilling(card.fillingType),
    )

    private fun toDomainColor(color: Slot.CardUiModel.Color): Card.Data.Color = when (color) {
        Slot.CardUiModel.Color.Primary -> Card.Data.Color.PRIMARY
        Slot.CardUiModel.Color.Secondary -> Card.Data.Color.SECONDARY
        Slot.CardUiModel.Color.Tertiary -> Card.Data.Color.TERTIARY
    }

    private fun toDomainFilling(filling: FillingTypeUiModel): Card.Data.Fill = when (filling) {
        FillingTypeUiModel.Filled -> Card.Data.Fill.SOLID
        FillingTypeUiModel.Striped -> Card.Data.Fill.STRIPED
        FillingTypeUiModel.Outlined -> Card.Data.Fill.EMPTY
    }

    private fun toDomainShape(shape: Slot.CardUiModel.Shape): Card.Data.Shape = when (shape) {
        Slot.CardUiModel.Shape.Oval -> Card.Data.Shape.OVAL
        Slot.CardUiModel.Shape.Diamond -> Card.Data.Shape.DIAMOND
        Slot.CardUiModel.Shape.Squiggle -> Card.Data.Shape.SQUIGGLE
    }
}
