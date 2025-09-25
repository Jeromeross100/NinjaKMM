package com.android.network

import io.ktor.client.HttpClient

// expect in common
expect fun createHttpClient(apiKey: String): HttpClient
