package com.android

import com.android.model.Animal
import com.android.model.Taxonomy
import com.android.network.AnimalDto
import com.android.network.TaxonomyDto

// Map a single DTO to your shared model
fun AnimalDto.toModel(): Animal = Animal(
    name = name,
    scientificName = scientific_name,           // <-- snake_case from DTO
    commonName = common_name,                   // <-- snake_case from DTO
    phylum = phylum ?: taxonomy?.phylum,        // prefer direct phylum, else taxonomy.phylum
    taxonomy = taxonomy?.toModel(),
    slogan = slogan,
    lifespan = lifespan,
    wingspan = wingspan,
    habitat = habitat,
    prey = prey,
    predators = predators
)

fun TaxonomyDto.toModel(): Taxonomy = Taxonomy(
    phylum = phylum
)

// Convenience for lists
fun List<AnimalDto>.toModels(): List<Animal> = map { it.toModel() }
