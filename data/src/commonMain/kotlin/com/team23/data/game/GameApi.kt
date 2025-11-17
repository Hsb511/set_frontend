package com.team23.data.game

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface GameApi {

    suspend fun createGame(sessionToken: Uuid, request: CreateGameRequest): CreateGameResponse
}

class GameApiImpl(
    private val client: HttpClient,
): GameApi {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createGame(sessionToken: Uuid, request: CreateGameRequest): CreateGameResponse {
        val response = client.post("https://settest.souchefr.synology.me/session/$sessionToken/create-game") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        println("HUGO - createGame: ${response.bodyAsText()}")
        return if (response.status.isSuccess()) {
            response.body<CreateGameResponse.Success>()
        } else {
            response.body<CreateGameResponse.Failure>()
        }
    }
}
