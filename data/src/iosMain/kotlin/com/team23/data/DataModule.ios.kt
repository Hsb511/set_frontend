package com.team23.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module

internal actual fun platformModule(): Module {
    TODO("Not yet implemented")
}

internal actual fun createHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }

        engine {
            // Similar to --insecure
            configureRequest {
                // This lambda is called per request
                setAllowsCellularAccess(true)
                setAllowsConstrainedNetworkAccess(true)
                setAllowsExpensiveNetworkAccess(true)
            }

            // Global config for insecure certs
            // (newer Ktor Darwin exposes this on the engineConfig)
            // This is roughly equivalent to "curl --insecure":
            // NOTE: exact API name may differ slightly by Ktor version.
            // If your version has `request.allowInsecureHTTPCertificate`,
            // set it in configureRequest {} like this:
            // configureRequest {
            //     handleChallenge { _, completionHandler ->
            //         completionHandler(NSURLSessionAuthChallengeDisposition.useCredential, null)
            //     }
            // }
        }
    }
}
