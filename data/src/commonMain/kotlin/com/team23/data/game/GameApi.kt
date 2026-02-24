package com.team23.data.game

import co.touchlab.kermit.Logger
import com.team23.data.game.model.request.CreateGameRequest
import com.team23.data.game.model.request.ParticipateInGameRequest
import com.team23.data.game.model.request.UploadDeckRequest
import com.team23.data.game.model.response.CreateGameResponse
import com.team23.data.game.model.response.GetGameResponse
import com.team23.data.game.model.response.GetLastDeckResponse
import com.team23.data.game.model.response.GetOpenGamesResponse
import com.team23.data.game.model.response.ParticipateInGameResponse
import com.team23.data.game.model.response.UploadDeckResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface GameApi {

    suspend fun getGame(sessionToken: Uuid): GetGameResponse
    suspend fun getLastDeck(sessionToken: Uuid): GetLastDeckResponse
    suspend fun createGame(sessionToken: Uuid, request: CreateGameRequest): CreateGameResponse
    suspend fun uploadDeck(sessionToken: Uuid, request: UploadDeckRequest): UploadDeckResponse
    suspend fun getOpenGames(sessionToken: Uuid): GetOpenGamesResponse
    suspend fun participateInGame(sessionToken: Uuid, request: ParticipateInGameRequest): ParticipateInGameResponse
}

@OptIn(ExperimentalUuidApi::class)
class GameApiImpl(
    private val client: HttpClient,
): GameApi {

    override suspend fun getGame(sessionToken: Uuid): GetGameResponse {
        val response = client.get("/session/$sessionToken/get-game")
        return if (response.status.isSuccess()) {
            Logger.i("GameApi - getGame Success - ${response.bodyAsText()}")
            response.body<GetGameResponse.Success>()
        } else {
            Logger.e("GameApi - getGame Error - ${response.bodyAsText()}")
            response.body<GetGameResponse.Failure>()
        }
    }

    override suspend fun getLastDeck(sessionToken: Uuid): GetLastDeckResponse {
        val response = client.get("/session/$sessionToken/get-last-deck")
        return if (response.status.isSuccess()) {
            Logger.i("GameApi - getLastDeck Success - ${response.bodyAsText()}")
            response.body<GetLastDeckResponse.Success>()
        } else {
            Logger.e("GameApi - getLastDeck Error - ${response.bodyAsText()}")
            response.body<GetLastDeckResponse.Failure>()
        }
    }

    override suspend fun createGame(sessionToken: Uuid, request: CreateGameRequest): CreateGameResponse {
        val response = client.post("/session/$sessionToken/create-game") {
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            Logger.i("GameApi - createGame Success - ${response.bodyAsText()}")
            response.body<CreateGameResponse.Success>()
        } else {
            Logger.e("GameApi - createGame Error - ${response.bodyAsText()}")
            response.body<CreateGameResponse.Failure>()
        }
    }

    override suspend fun uploadDeck(sessionToken: Uuid, request: UploadDeckRequest): UploadDeckResponse {
        val urlString = "/session/$sessionToken/upload-deck"
        Logger.d("GameApi - $urlString - request: ${Json.encodeToString(request)}")
        val response = client.post(urlString) {
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            Logger.i("GameApi - uploadDeck Success - ${response.bodyAsText()}")
            response.body<UploadDeckResponse.Success>()
        } else {
            Logger.e("GameApi - uploadDeck Error - ${response.bodyAsText()}")
            response.body<UploadDeckResponse.Failure>()
        }
    }

    override suspend fun getOpenGames(sessionToken: Uuid): GetOpenGamesResponse {
        val urlString = "/session/$sessionToken/get-open-games"
        Logger.d("GameApi - $urlString")
        val response = client.get(urlString)
        return if (response.status.isSuccess()) {
            Logger.i("GameApi - getOpenGames Success - ${response.bodyAsText()}")
            response.body<GetOpenGamesResponse.Success>()
        } else {
            Logger.e("GameApi - getOpenGames Error - ${response.bodyAsText()}")
            response.body<GetOpenGamesResponse.Failure>()
        }
    }

    override suspend fun participateInGame(sessionToken: Uuid, request: ParticipateInGameRequest): ParticipateInGameResponse {
        val urlString = "/session/$sessionToken/participate-in-game"
        Logger.i("GameApi - $urlString - request: ${Json.encodeToString(request)}")
        val response = client.post(urlString) {
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            Logger.i("GameApi - participateInGame Success - ${response.bodyAsText()}")
            response.body<ParticipateInGameResponse.Success>()
        } else {
            Logger.e("GameApi - participateInGame Error - ${response.bodyAsText()}")
            response.body<ParticipateInGameResponse.Failure>()
        }
    }
}
