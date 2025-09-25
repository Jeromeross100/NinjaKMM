// shared/src/commonMain/kotlin/com/android/network/ApiModels.kt
package com.android.network

import kotlinx.serialization.Serializable

@Serializable
data class AnimalDto(
    val name: String? = null,
    val taxonomy: TaxonomyDto? = null,

    // API uses snake_case; keep snake_case to avoid extra @SerialName
    val scientific_name: String? = null,
    val common_name: String? = null,

    // Some responses give both top-level phylum and taxonomy.phylum
    val phylum: String? = null,

    // Category-specific fields:
    val slogan: String? = null,     // dogs
    val lifespan: String? = null,   // dogs
    val wingspan: String? = null,   // birds
    val habitat: String? = null,    // birds
    val prey: List<String>? = null, // bugs
    val predators: List<String>? = null
)

@Serializable
data class TaxonomyDto(
    val phylum: String? = null
)
