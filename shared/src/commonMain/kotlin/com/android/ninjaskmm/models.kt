package com.android.ninjaskmm

// commonMain/src/.../models.kt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Animal(
    val name: String,
    val taxonomy: Taxonomy? = null,
    @SerialName("common_name") val commonName: String? = null,
    val habitat: String? = null,
    val lifespan: String? = null,
    val slogan: String? = null,
    val wingspan: String? = null,
    val prey: List<String>? = null,
    val predators: List<String>? = null,
    val phylum: String? = null,
    @SerialName("scientific_name") val scientificName: String? = null,
    // ... API has many other fields, keep only what we need
)

@Serializable
data class Taxonomy(
    val phylum: String? = null
)
