package com.team23.domain.game.repository

import com.team23.domain.game.statemachine.GameState

interface GameRepository {

    suspend fun createSoloGame(): Result<GameState.Playing>

}