package com.team23.data.game

import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.statemachine.GameState

class GameRepositoryImpl: GameRepository {

    override suspend fun createSoloGame(): Result<GameState.Playing> {
        return Result.success(initializeGame())
    }

    private fun initializeGame(): GameState.Playing {
        // TODO CALL BACKEND
        return GameState.Playing(deck = emptyList(), table = emptyList())
    }

}
