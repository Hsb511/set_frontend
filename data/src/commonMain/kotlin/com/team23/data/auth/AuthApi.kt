package com.team23.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

interface AuthApi {
    suspend fun register(request: AuthRegisterRequest): AuthRegisterResponse
}

class AuthApiImpl(
    private val client: HttpClient
) : AuthApi {

    override suspend fun register(request: AuthRegisterRequest): AuthRegisterResponse {
        val response = client.post("https://settest.souchefr.synology.me/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        println("HUGO - response.status: ${response.status}")
        return if (response.status.isSuccess()) {
            response.body<AuthRegisterResponse.Success>()
        } else {
            response.body<AuthRegisterResponse.Failure>()
        }
    }
}
