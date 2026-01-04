package com.team23.data

import com.team23.data.datastore.SetDataStore
import com.team23.data.datastore.AndroidSetDataStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager


internal actual fun platformModule() = module {
    single { AndroidSetDataStore() as SetDataStore }
}

actual fun createHttpClient(): HttpClient {
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
            url(BuildKonfig.BASE_URL)
            contentType(ContentType.Application.Json)
        }

        engine {
            https {
                trustManager = object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                }
            }
        }
    }
}

actual fun getBaseUrl(): String {
    return BuildKonfig.BASE_URL
}

actual fun getVersionName(): String {
    return BuildKonfig.VERSION_NAME
}