package com.team23.data.game

import com.team23.data.card.CardDataMapper
import com.team23.data.card.SetCard
import com.team23.data.datastore.SetDataStore
import com.team23.data.game.model.request.CreateGameRequest
import com.team23.data.game.model.request.UploadDeckRequest
import com.team23.data.game.model.response.CreateGameResponse
import com.team23.data.game.model.response.GetGameResponse
import com.team23.data.game.model.response.GetLastDeckResponse
import com.team23.data.game.model.response.GetOpenGamesResponse
import com.team23.data.game.model.response.UploadDeckResponse
import com.team23.domain.game.model.Card
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.statemachine.GameState
import com.team23.domain.startup.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GameRepositoryImpl(
    private val gameApi: GameApi,
    private val authRepository: AuthRepository,
    private val setDataStore: SetDataStore,
    private val cardDataMapper: CardDataMapper,
) : GameRepository {

    override suspend fun getOngoingSoloGame(): Result<GameState.Playing> = runCatching {
        getOngoingSoloGameAndRetry(retry = true)
    }

    override suspend fun createSoloGame(force: Boolean): Result<GameState.Playing> = runCatching {
        createSoloGameAndRetry(force = force, retry = true)
    }

    override suspend fun notifySoloGameUpdated(game: GameState.Playing): Result<Unit> = runCatching {
        notifySoloGameUpdatedAndRetry(game, retry = true)
    }

    override suspend fun notifySoloGameFinished(finished: GameState.Finished): Result<Boolean> = runCatching {
        notifySoloGameFinishedAndRetry(finished, retry = true)
    }

    override suspend fun hasActiveSoloGame(): Result<Unit> = runCatching {
        hasActiveSoloGameAndRetry(retry = true)
    }

    override suspend fun createMultiGame(): Result<Uuid> = runCatching {
        createMultiGameAndRetry(retry = true)
    }

    override fun publicMultiGames(): Flow<List<String>> = flow {
        // TODO PING EVERY SECONDS?
        runCatching {
            val sessionToken = getCachedSessionToken()
            val publicGames = when (val response = gameApi.getOpenGames(sessionToken)) {
                is GetOpenGamesResponse.Success -> response.games
                is GetOpenGamesResponse.Failure -> emptyList()
                is GetOpenGamesResponse.InvalidSessionToken -> emptyList()
            }
            emit(publicGames)
        }
    }

    private suspend fun getOngoingSoloGameAndRetry(retry: Boolean): GameState.Playing {
        val sessionToken = getCachedSessionToken()
        return when (val gameResponse = gameApi.getGame(sessionToken)) {
            is GetGameResponse.Success -> when (val response = gameApi.getLastDeck(sessionToken)) {
                is GetLastDeckResponse.Success -> mapToPlayingGame(
                    gameId = gameResponse.gameId,
                    table = response.table,
                    pile = response.pile,
                    pit = response.pit,
                )

                is GetLastDeckResponse.Failure -> throw Exception(response.error)
                is GetLastDeckResponse.InvalidSessionToken -> throw RefreshSessionTokenException()
            }

            is GetGameResponse.Failure -> throw Exception(gameResponse.error)
            is GetGameResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                getOngoingSoloGameAndRetry(retry = false)
            }
        }
    }

    private suspend fun createSoloGameAndRetry(force: Boolean, retry: Boolean): GameState.Playing {
        val sessionToken = getCachedSessionToken()
        val request = CreateGameRequest(
            gameMode = CreateGameRequest.GameMode.Solo,
            responseMode = CreateGameRequest.ResponseMode.Full,
            force = force,
        )
        return when (val response = gameApi.createGame(sessionToken, request)) {
            is CreateGameResponse.Success -> mapToPlayingGame(
                gameId = response.gameId,
                table = response.table,
                pile = response.pile,
            )

            is CreateGameResponse.Failure -> throw Exception(response.error)
            is CreateGameResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                createSoloGameAndRetry(force = force, retry = false)
            }
        }
    }

    private suspend fun notifySoloGameUpdatedAndRetry(game: GameState.Playing, retry: Boolean): Boolean {
        val sessionToken = getCachedSessionToken()
        val request = mapToNextUploadRequest(game)
        return when (val response = gameApi.uploadDeck(sessionToken, request)) {
            is UploadDeckResponse.Success -> response.gameCompleted
            is UploadDeckResponse.Failure -> throw Exception(response.error)
            is UploadDeckResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                notifySoloGameUpdatedAndRetry(game, retry = false)
            }
        }
    }

    private suspend fun notifySoloGameFinishedAndRetry(finished: GameState.Finished, retry: Boolean): Boolean {
        val sessionToken = getCachedSessionToken()
        val request = mapToFinalUploadRequest(finished)

        return when (val response = gameApi.uploadDeck(sessionToken, request)) {
            is UploadDeckResponse.Success -> response.gameCompleted
            is UploadDeckResponse.Failure -> throw Exception(response.error)
            is UploadDeckResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                notifySoloGameFinishedAndRetry(finished, retry = false)
            }
        }
    }

    private suspend fun hasActiveSoloGameAndRetry(retry: Boolean) {
        val sessionToken = getCachedSessionToken()
        return when (val response = gameApi.getGame(sessionToken)) {
            is GetGameResponse.Success -> Unit
            is GetGameResponse.Failure ->  throw Exception(response.error)
            is GetGameResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                hasActiveSoloGameAndRetry(retry = false)
            }
        }
    }

    private suspend fun createMultiGameAndRetry(retry: Boolean): Uuid {
        val sessionToken = getCachedSessionToken()
        val request = CreateGameRequest(
            gameMode = CreateGameRequest.GameMode.Multi,
            responseMode = CreateGameRequest.ResponseMode.Full,
            force = false,
        )
        return when (val response = gameApi.createGame(sessionToken, request)) {
            is CreateGameResponse.Success -> response.gameId
            is CreateGameResponse.Failure -> throw Exception(response.error)
            is CreateGameResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                createMultiGameAndRetry(retry = false)
            }
        }
    }

    private suspend fun getCachedSessionToken(): Uuid {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        return Uuid.parse(cachedSessionToken)
    }

    private fun mapToPlayingGame(
        gameId: Uuid,
        table: List<SetCard>?,
        pile: List<SetCard>?,
        pit: List<List<SetCard>> = emptyList(),
    ): GameState.Playing {
        requireNotNull(table)
        requireNotNull(pile)
        return GameState.Playing(
            gameId = gameId,
            deck = pile.map(cardDataMapper::toDomainModel),
            table = table.map(cardDataMapper::toDomainModel),
            setsFound = pit.map { set -> set.map(cardDataMapper::toDomainModel).filterIsInstance<Card.Data>().toSet() }
        )
    }

    private fun mapToNextUploadRequest(game: GameState.Playing) = UploadDeckRequest(
        uploadMode = UploadDeckRequest.UploadMode.Next,
        pile = game.deck.map(cardDataMapper::toBase10Code),
        table = game.table.map(cardDataMapper::toBase10Code),
        pit = game.setsFound.map { set ->
            set.map(cardDataMapper::toBase10Code)
        },
    )

    private fun mapToFinalUploadRequest(finished: GameState.Finished) = UploadDeckRequest(
        uploadMode = UploadDeckRequest.UploadMode.Final,
        pile = emptyList(),
        table = finished.cards.map(cardDataMapper::toBase10Code),
        pit = finished.setsFound.map { set ->
            set.map(cardDataMapper::toBase10Code)
        },
    )

    private suspend fun <T> handleRetryMechanism(retry: Boolean, run: suspend () -> T): T {
        if (retry && tryRefreshSessionToken()) {
            return run()
        } else {
            throw RefreshSessionTokenException()
        }
    }

    private suspend fun tryRefreshSessionToken(): Boolean {
        val username = setDataStore.getValue(SetDataStore.USERNAME_KEY)
        val password = setDataStore.getValue(SetDataStore.PASSWORD_KEY)

        if (username == null || password == null) return false
        return authRepository.loginAndStoreUserInfo(username, password).isSuccess
    }

    private class RefreshSessionTokenException : Exception("Session token has expired and couldn't refresh it")
}
