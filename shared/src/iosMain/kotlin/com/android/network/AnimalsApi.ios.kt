package com.android.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// iOS-specific actual implementation
actual fun createAnimalsApi(apiKey: String): AnimalsApi {
    val client = HttpClient(Darwin) {
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
                protocol = URLProtocol.HTTPS
                host = "api.api-ninjas.com"
                appendPathSegments("v1")
            }
            headers {
                append("X-Api-Key", apiKey)
                accept(ContentType.Application.Json)
            }
        }
    }

    return AnimalsApi(client)
}
