package com.team23.data.game

import com.team23.data.card.CardDataMapper
import com.team23.data.card.SetCard
import com.team23.data.datastore.SetDataStore
import com.team23.data.game.model.request.CreateGameRequest
import com.team23.data.game.model.request.UploadDeckRequest
import com.team23.data.game.model.response.CreateGameResponse
import com.team23.data.game.model.response.GetGameResponse
import com.team23.data.game.model.response.GetLastDeckResponse
import com.team23.data.game.model.response.UploadDeckResponse
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.statemachine.GameState
import com.team23.domain.startup.repository.AuthRepository
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

    override suspend fun createSoloGame(): Result<GameState.Playing> = runCatching {
        createSoloGameAndRetry(retry = true)
    }

    override suspend fun notifySoloGameFinished(finished: GameState.Finished): Result<Boolean> = runCatching {
        notifySoloGameFinishedAndRetry(finished, retry = true)
    }

    override suspend fun hasActiveSoloGame(): Result<Unit> = runCatching {
        hasActiveSoloGameAndRetry(retry = true)
    }

    private suspend fun getOngoingSoloGameAndRetry(retry: Boolean): GameState.Playing {
        val sessionToken = getCachedSessionToken()
        return when (val gameResponse = gameApi.getGame(sessionToken)) {
            is GetGameResponse.Success -> when (val response = gameApi.getLastDeck(sessionToken)) {
                is GetLastDeckResponse.Success -> mapToPlayingGame(
                    gameId = gameResponse.gameId,
                    table = response.table,
                    pile = response.pile
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

    private suspend fun createSoloGameAndRetry(retry: Boolean): GameState.Playing {
        val sessionToken = getCachedSessionToken()
        val request = CreateGameRequest(CreateGameRequest.GameMode.Solo, CreateGameRequest.ResponseMode.Full)
        return when (val response = gameApi.createGame(sessionToken, request)) {
            is CreateGameResponse.Success -> mapToPlayingGame(
                gameId = response.gameId,
                table = response.table,
                pile = response.pileCards
            )

            is CreateGameResponse.Failure -> throw Exception(response.error)
            is CreateGameResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                createSoloGameAndRetry(retry = false)
            }
        }
    }

    private suspend fun notifySoloGameFinishedAndRetry(finished: GameState.Finished, retry: Boolean): Boolean {
        val sessionToken = getCachedSessionToken()
        val request = mapToUploadRequest(finished)
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

    private suspend fun getCachedSessionToken(): Uuid {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        return Uuid.parse(cachedSessionToken)
    }

    private fun mapToPlayingGame(gameId: Uuid, table: List<SetCard>?, pile: List<SetCard>?): GameState.Playing {
        requireNotNull(table)
        requireNotNull(pile)
        return GameState.Playing(
            gameId = gameId,
            deck = pile.map(cardDataMapper::toDomainModel),
            table = table.map(cardDataMapper::toDomainModel),
        )
    }

    private fun mapToUploadRequest(finished: GameState.Finished) = UploadDeckRequest(
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
