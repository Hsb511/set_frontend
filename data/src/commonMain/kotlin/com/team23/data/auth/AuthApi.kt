package com.team23.data.auth

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

interface AuthApi {
    suspend fun register(request: AuthRequest): AuthRegisterResponse
    suspend fun signin(request: AuthRequest): AuthSignResponse
}

class AuthApiImpl(
    private val client: HttpClient,
) : AuthApi {

    override suspend fun register(request: AuthRequest): AuthRegisterResponse {
        val response = client.post("/register") {
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            Logger.i("AuthApi - register Success - ${response.bodyAsText()}")
            response.body<AuthRegisterResponse.Success>()
        } else {
            Logger.e("AuthApi - register Failure - ${response.bodyAsText()}")
            response.body<AuthRegisterResponse.Failure>()
        }
    }

    override suspend fun signin(request: AuthRequest): AuthSignResponse {
        val response = client.post("/signin") {
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            Logger.i("AuthApi - signin Success - ${response.bodyAsText()}")
            response.body<AuthSignResponse.Success>()
        } else {
            Logger.e("AuthApi - signin Failure - ${response.bodyAsText()}")
            response.body<AuthSignResponse.Failure>()
        }
    }
}
