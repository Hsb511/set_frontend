package com.team23.data

import com.team23.data.datastore.SetDataStore
import com.team23.data.datastore.JvmSetDataStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal actual fun platformModule() = module {
    single { JvmSetDataStore() as SetDataStore }
}


internal actual fun createHttpClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }

        install(DefaultRequest) {
            url {
                takeFrom(BuildKonfig.BASE_URL)
            }
            contentType(ContentType.Application.Json)
        }
    }
}

actual fun getBaseUrl(): String {
    return BuildKonfig.BASE_URL
}

actual fun getVersionName(): String {
    return BuildKonfig.VERSION_NAME
}