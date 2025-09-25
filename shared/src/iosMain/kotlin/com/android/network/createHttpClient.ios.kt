package com.android.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun createHttpClient(apiKey: String): HttpClient = HttpClient(Darwin) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                explicitNulls = false
            }
        )
    }
    install(Logging) { level = LogLevel.INFO }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 30_000
    }

    // ✅ Ktor 2.x: DefaultRequest plugin
    install(DefaultRequest) {
        header("X-Api-Key", apiKey)
        accept(ContentType.Application.Json)
        url("https://api.api-ninjas.com/v1/") // base URL
    }
}
