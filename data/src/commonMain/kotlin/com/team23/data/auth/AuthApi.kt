package com.team23.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

interface AuthApi {
    suspend fun register(request: AuthRequest): AuthRegisterResponse
    suspend fun signin(request: AuthRequest): AuthSignResponse
}

class AuthApiImpl(
    private val client: HttpClient,
) : AuthApi {

    override suspend fun register(request: AuthRequest): AuthRegisterResponse {
        val response = client.post("https://settest.souchefr.synology.me/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            response.body<AuthRegisterResponse.Success>()
        } else {
            response.body<AuthRegisterResponse.Failure>()
        }
    }

    override suspend fun signin(request: AuthRequest): AuthSignResponse {
        val response = client.post("https://settest.souchefr.synology.me/signin") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            response.body<AuthSignResponse.Success>()
        } else {
            response.body<AuthSignResponse.Failure>()
        }
    }
}
