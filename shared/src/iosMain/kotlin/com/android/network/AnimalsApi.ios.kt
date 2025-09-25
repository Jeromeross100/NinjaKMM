// shared/src/iosMain/kotlin/com/android/network/AnimalsApi.ios.kt
package com.android.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClientIos(apiKey: String): HttpClient = HttpClient(Darwin) {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; explicitNulls = false }) }
    install(Logging) { level = LogLevel.INFO }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 30_000
    }
    defaultRequest {
        url { protocol = URLProtocol.HTTPS; host = "api.api-ninjas.com"; path("v1") }
        header("X-Api-Key", apiKey)
        accept(ContentType.Application.Json)
    }
}