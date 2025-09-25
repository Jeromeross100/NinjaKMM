// shared/src/commonMain/kotlin/com/android/repository/AnimalsRepository.kt
package com.android.repository

import com.android.ninjaskmm.Animal
import com.russhwolf.settings.Settings
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock

interface AnimalsApi {
    suspend fun fetchByName(name: String): List<Animal> // already mapped to your shared model
}

class AnimalsRepository(
    private val api: AnimalsApi,
    private val settings: Settings,
    private val cacheKey: String = "animals_cache_json",
    private val cacheTimeKey: String = "animals_cache_time",
    private val cacheTtlMillis: Long = 10 * 60 * 1000 // 10 minutes
) {
    private val mutex = Mutex()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getAnimals(forceRefresh: Boolean = false): List<Animal> = mutex.withLock {
        val now = Clock.System.now().toEpochMilliseconds()
        if (!forceRefresh) {
            val cachedAt = settings.getLongOrNull(cacheTimeKey)
            val cachedJson = settings.getStringOrNull(cacheKey)
            if (cachedAt != null && cachedJson != null && now - cachedAt < cacheTtlMillis) {
                return@withLock json.decodeFromString(cachedJson)
            }
        }

        // Fetch at least 3 from each category
        val categories = listOf("dog", "bird", "bug")
        val fresh = coroutineScope {
            val jobs = categories.map { name ->
                async { api.fetchByName(name).take(3) }
            }
            jobs.flatMap { it.await() } // 3 * 3 = 9+
        }

        settings.putString(cacheKey, json.encodeToString(fresh))
        settings.putLong(cacheTimeKey, now)
        fresh
    }
}
