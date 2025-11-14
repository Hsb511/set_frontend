package com.team23.domain.game

import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape

object GameTestUtils {

    fun createCard(number: Int, color: Color, shape: Shape, fill: Fill) =
        Card.Data(color, shape, number, fill)
}