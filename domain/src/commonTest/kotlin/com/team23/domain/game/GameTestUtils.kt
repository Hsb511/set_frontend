package com.team23.domain.game

import com.team23.domain.game.model.Card
import com.team23.domain.game.model.Card.Data.Color
import com.team23.domain.game.model.Card.Data.Fill
import com.team23.domain.game.model.Card.Data.Shape
import com.team23.domain.game.statemachine.GameState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object GameTestUtils {

    fun createCard(number: Int, color: Color, shape: Shape, fill: Fill) =
        Card.Data(color, shape, number, fill)

    fun createPlayingGame(
        table: List<Card>,
        deck: List<Card>,
    ) = GameState.Playing(
        gameId = Uuid.random(),
        deck = deck,
        table = table,
    )
}