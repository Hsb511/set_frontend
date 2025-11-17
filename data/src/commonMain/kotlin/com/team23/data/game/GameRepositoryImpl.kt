package com.team23.data.game

import com.team23.data.card.CardDataMapper
import com.team23.data.datastore.SetDataStore
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.game.statemachine.GameState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GameRepositoryImpl(
    private val gameApi: GameApi,
    private val setDataStore: SetDataStore,
    private val cardDataMapper: CardDataMapper,
) : GameRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createSoloGame(): Result<GameState.Playing> = runCatching {
        val cachedSessionToken = setDataStore.getValue(SetDataStore.SESSION_TOKEN_KEY)
        requireNotNull(cachedSessionToken)
        val sessionToken = Uuid.parse(cachedSessionToken)
        val request = CreateGameRequest(CreateGameRequest.GameMode.Solo, CreateGameRequest.ResponseMode.Full)
        val response = gameApi.createGame(sessionToken, request)
        when (response) {
            is CreateGameResponse.Success -> {
                println("HUGO - table: ${response.table}")
                requireNotNull(response.table)
                println("HUGO - table: ${response.pileCards}")
                requireNotNull(response.pileCards)
                GameState.Playing(
                    deck = response.pileCards.map(cardDataMapper::toDomainModel),
                    table = response.table.map(cardDataMapper::toDomainModel),
                )
            }
            is CreateGameResponse.Failure -> throw Exception(response.error)
        }
    }
}
