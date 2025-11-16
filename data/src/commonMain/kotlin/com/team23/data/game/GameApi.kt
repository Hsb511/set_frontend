package com.team23.data.game

import com.team23.data.auth.AuthRegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

interface GameApi {
    suspend fun createGame(request: CreateGameRequest): CreateGameResponse
}

class GameApiImpl(
    private val client: HttpClient
): GameApi {

    override suspend fun createGame(request: CreateGameRequest): CreateGameResponse {
        val response = client.post("https://settest.souchefr.synology.me/create-game") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        println("HUGO - createGame: $response")
        return if (response.status.isSuccess()) {
            response.body<CreateGameResponse.Success>()
        } else {
            response.body<CreateGameResponse.Failure>()
        }
    }
}
