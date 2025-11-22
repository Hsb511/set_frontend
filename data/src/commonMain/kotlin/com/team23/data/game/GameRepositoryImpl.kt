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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GameRepositoryImpl(
    private val gameApi: GameApi,
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

    private suspend fun getOngoingSoloGameAndRetry(retry: Boolean): GameState.Playing {
        val sessionToken = getCachedSessionToken()
        return when (val gameResponse = gameApi.getGame(sessionToken)) {
            is GetGameResponse.Success -> when (val response = gameApi.getLastDeck(sessionToken)) {
                is GetLastDeckResponse.Success -> mapToPlayingGame(
                    gameId = response.gameId,
                    table = response.table,
                    pileCards = response.pileCards
                )

                is GetLastDeckResponse.Failure -> throw Exception(response.error)
                is GetLastDeckResponse.InvalidSessionToken -> throw RefreshSessionTokenException
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
        val response = gameApi.createGame(sessionToken, request)
        return when (response) {
            is CreateGameResponse.Success -> mapToPlayingGame(
                gameId = response.gameId,
                table = response.table,
                pileCards = response.pileCards
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
        val response = gameApi.uploadDeck(sessionToken, request)
        return when (response) {
            is UploadDeckResponse.Success -> response.gameCompleted
            is UploadDeckResponse.Failure -> throw Exception(response.error)
            is UploadDeckResponse.InvalidSessionToken -> handleRetryMechanism(retry) {
                notifySoloGameFinishedAndRetry(finished, retry = false)
            }
        }
    }

    private suspend fun getCachedSessionToken(): Uuid {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        return Uuid.parse(cachedSessionToken)
    }

    private fun mapToPlayingGame(gameId: Uuid, table: List<SetCard>?, pileCards: List<SetCard>?): GameState.Playing {
        requireNotNull(table)
        requireNotNull(pileCards)
        return GameState.Playing(
            gameId = gameId,
            deck = pileCards.map(cardDataMapper::toDomainModel),
            table = table.map(cardDataMapper::toDomainModel),
        )
    }

    private fun mapToUploadRequest(finished: GameState.Finished) = UploadDeckRequest(
        gameId = finished.gameId,
        uploadMode = UploadDeckRequest.UploadMode.Final,
        turn = finished.setsFound.size + 1,
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
            throw RefreshSessionTokenException
        }
    }

    private suspend fun tryRefreshSessionToken(): Boolean {
        return false
    }

    private object RefreshSessionTokenException : Exception("Session token has expired and couldn't refresh it")
}
