package com.team23.data.admin

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

interface AdminApi {

    suspend fun clear(request: AdminClearRequest): AdminClearResponse
}

class AdminApiImpl(
    private val client: HttpClient,
) : AdminApi {

    override suspend fun clear(request: AdminClearRequest): AdminClearResponse {
        val response = client.post("https://settest.souchefr.synology.me/clear") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            response.body<AdminClearResponse.Success>()
        } else {
            response.body<AdminClearResponse.Failure>()
        }
    }
}
