// shared/src/commonMain/kotlin/com/android/network/AnimalsApi.kt
package com.android.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class AnimalsApi(private val client: HttpClient) {
    suspend fun fetchByName(name: String): List<AnimalDto> {
        val resp = client.get("animals") { parameter("name", name) }
        val ct = resp.headers[HttpHeaders.ContentType] ?: ""
        if (!resp.status.isSuccess() || !ct.startsWith(ContentType.Application.Json.contentType)) {
            val body = resp.bodyAsText()
            throw IllegalStateException("Expected JSON but got '$ct' (${resp.status}). Body:\n$body")
        }
        return resp.body()
    }
}

// Expect a factory per platform that provides a client with base URL + headers
expect fun createAnimalsApi(apiKey: String): AnimalsApi
