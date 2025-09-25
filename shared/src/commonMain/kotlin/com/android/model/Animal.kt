package com.android.model

import kotlinx.serialization.Serializable

@Serializable
data class Animal(
    val name: String? = null,
    val scientificName: String? = null,
    val commonName: String? = null,
    val phylum: String? = null,
    val taxonomy: Taxonomy? = null,
    val slogan: String? = null,
    val lifespan: String? = null,
    val wingspan: String? = null,
    val habitat: String? = null,
    val prey: List<String>? = null,
    val predators: List<String>? = null
)

@Serializable
data class Taxonomy(
    val phylum: String? = null
)
