package com.team23.data.admin

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

interface AdminApi {

    suspend fun clear(request: AdminClearRequest): AdminClearResponse
}

class AdminApiImpl(
    private val client: HttpClient,
) : AdminApi {

    override suspend fun clear(request: AdminClearRequest): AdminClearResponse {
        val response = client.post("/clear") {
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            Logger.e("AdminApi - clear Success - ${response.bodyAsText()}")
            response.body<AdminClearResponse.Success>()
        } else {
            Logger.e("AdminApi - clear Failure - ${response.bodyAsText()}")
            response.body<AdminClearResponse.Failure>()
        }
    }
}
