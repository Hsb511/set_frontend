package com.team23.data.game

import com.team23.data.card.CardDataMapper
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

    private suspend fun getOngoingSoloGameAndRetry(retry: Boolean): GameState.Playing {
        val cachedSessionToken =  setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        val sessionToken = Uuid.parse(cachedSessionToken)
        return when (val gameResponse = gameApi.getGame(sessionToken)) {
            is GetGameResponse.Success -> when (val response = gameApi.getLastDeck(sessionToken)) {
                is GetLastDeckResponse.Success -> {
                    requireNotNull(response.table)
                    requireNotNull(response.pileCards)
                    GameState.Playing(
                        gameId = response.gameId,
                        deck = response.pileCards.map(cardDataMapper::toDomainModel),
                        table = response.table.map(cardDataMapper::toDomainModel),
                    )
                }
                is GetLastDeckResponse.Failure -> throw Exception(response.error)
                is GetLastDeckResponse.InvalidSessionToken -> throw RefreshSessionTokenException
            }
            is GetGameResponse.Failure -> throw Exception(gameResponse.error)
            is GetGameResponse.InvalidSessionToken -> if (retry && tryRefreshSessionToken()) {
                getOngoingSoloGameAndRetry(retry = false)
            } else {
                throw RefreshSessionTokenException
            }
        }
    }

    override suspend fun createSoloGame(): Result<GameState.Playing> = runCatching {
        createSoloGameAndRetry(retry = true)
    }

    private suspend fun createSoloGameAndRetry(retry: Boolean): GameState.Playing {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        val sessionToken = Uuid.parse(cachedSessionToken)
        val request = CreateGameRequest(CreateGameRequest.GameMode.Solo, CreateGameRequest.ResponseMode.Full)
        val response = gameApi.createGame(sessionToken, request)
        return when (response) {
            is CreateGameResponse.Success -> {
                requireNotNull(response.table)
                requireNotNull(response.pileCards)
                GameState.Playing(
                    gameId = response.gameId,
                    deck = response.pileCards.map(cardDataMapper::toDomainModel),
                    table = response.table.map(cardDataMapper::toDomainModel),
                )
            }

            is CreateGameResponse.Failure -> throw Exception(response.error)
            is CreateGameResponse.InvalidSessionToken -> if (retry && tryRefreshSessionToken()) {
                createSoloGameAndRetry(retry = false)
            } else {
                throw RefreshSessionTokenException
            }
        }
    }

    override suspend fun notifySoloGameFinished(finished: GameState.Finished): Result<Boolean> = runCatching {
        notifySoloGameFinishedAndRetry(finished, retry = true)
    }

    private suspend fun notifySoloGameFinishedAndRetry(finished: GameState.Finished, retry: Boolean): Boolean {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        val sessionToken = Uuid.parse(cachedSessionToken)
        val request = UploadDeckRequest(
            gameId = finished.gameId,
            uploadMode = UploadDeckRequest.UploadMode.Final,
            turn = finished.setsFound.size + 1,
            pile = emptyList(),
            table = finished.cards.map(cardDataMapper::toBase10Code),
            pit = finished.setsFound.map { set ->
                set.map(cardDataMapper::toBase10Code)
            },
        )
        val response = gameApi.uploadDeck(sessionToken, request)
        return when (response) {
            is UploadDeckResponse.Success -> response.gameCompleted
            is UploadDeckResponse.Failure -> throw Exception(response.error)
            is UploadDeckResponse.InvalidSessionToken -> if (retry && tryRefreshSessionToken()) {
                notifySoloGameFinishedAndRetry(finished, retry = false)
            } else {
                throw RefreshSessionTokenException
            }
        }
    }

    private suspend fun tryRefreshSessionToken(): Boolean {
        return false
    }

    private object RefreshSessionTokenException: Exception("Session token has expired and couldn't refresh it")
}
