package com.team23.data.game

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface GameApi {

    suspend fun createGame(sessionToken: Uuid, request: CreateGameRequest): CreateGameResponse

    suspend fun uploadDeck(sessionToken: Uuid, request: UploadDeckRequest): UploadDeckResponse
}

@OptIn(ExperimentalUuidApi::class)
class GameApiImpl(
    private val client: HttpClient,
): GameApi {

    override suspend fun createGame(sessionToken: Uuid, request: CreateGameRequest): CreateGameResponse {
        val response = client.post("/session/$sessionToken/create-game") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            response.body<CreateGameResponse.Success>()
        } else {
            response.body<CreateGameResponse.Failure>()
        }
    }

    override suspend fun uploadDeck(sessionToken: Uuid, request: UploadDeckRequest): UploadDeckResponse {
        val response = client.post("/session/$sessionToken/upload-deck") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            response.body<UploadDeckResponse.Success>()
        } else {
            response.body<UploadDeckResponse.Failure>()
        }
    }
}
