package com.team23.domain.game.repository

import com.team23.domain.game.statemachine.GameState

interface GameRepository {

    suspend fun getOngoingSoloGame(): Result<GameState.Playing>

    suspend fun createSoloGame(): Result<GameState.Playing>

    suspend fun notifySoloGameFinished(finished: GameState.Finished): Result<Boolean>
}