package com.team23.data.game

import com.team23.data.card.CardDataMapper
import com.team23.data.datastore.SetDataStore
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
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        val sessionToken = Uuid.parse(cachedSessionToken)
        when (val gameResponse = gameApi.getGame(sessionToken)) {
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
            }
            is GetGameResponse.Failure -> throw Exception(gameResponse.error)
        }
    }

    override suspend fun createSoloGame(): Result<GameState.Playing> = runCatching {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        val sessionToken = Uuid.parse(cachedSessionToken)
        val request = CreateGameRequest(CreateGameRequest.GameMode.Solo, CreateGameRequest.ResponseMode.Full)
        val response = gameApi.createGame(sessionToken, request)
        when (response) {
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
        }
    }

    override suspend fun notifySoloGameFinished(finished: GameState.Finished): Result<Boolean> = runCatching {
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
        when (response) {
            is UploadDeckResponse.Success -> response.gameCompleted
            is UploadDeckResponse.Failure -> throw Exception(response.error)
        }
    }
}
