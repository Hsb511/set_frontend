package com.team23.domain.game.repository

import com.team23.domain.game.model.GameMode
import com.team23.domain.game.statemachine.GameState
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface GameRepository {

    // SOLO
    suspend fun getOngoingSoloGame(): Result<GameState.Playing>

    suspend fun createSoloGame(force: Boolean): Result<GameState.Playing>

    suspend fun notifySoloGameUpdated(game: GameState.Playing): Result<Unit>

    suspend fun notifySoloGameFinished(finished: GameState.Finished): Result<Boolean>

    suspend fun hasActiveSoloGame(): Result<Unit>

    // MULTI
    @OptIn(ExperimentalUuidApi::class)
    suspend fun createMultiGame(gameMode: GameMode): Result<Uuid>

    fun publicMultiGames(): Flow<List<String>>
}