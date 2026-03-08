package com.team23.domain.game.repository

import com.team23.domain.game.model.GameMode
import com.team23.domain.game.model.MultiGameMessage
import com.team23.domain.game.statemachine.GameState
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface GameRepository {

    // SOLO
    suspend fun getOngoingSoloGame(): Result<GameState.Playing>

    suspend fun createSoloGame(force: Boolean): Result<GameState.Playing>

    suspend fun notifySoloGameUpdated(game: GameState.Playing): Result<Unit>

    suspend fun notifySoloGameFinished(finished: GameState.Finished): Result<Boolean>

    suspend fun hasActiveSoloGame(): Result<Unit>

    // MULTI
    val multiGameMessages: Flow<MultiGameMessage>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun createMultiGame(gameMode: GameMode): Result<Pair<GameState.Playing, String>>

    fun publicMultiGames(): Flow<List<String>>

    suspend fun joinMultiGame(publicName: String): Result<Pair<GameState.Playing, List<String>>>

    suspend fun switchToWebSocket(): Result<Unit>

    suspend fun startGame(gameId: Uuid, startTime: Instant)

    suspend fun leaveGame(): Result<Unit>
}