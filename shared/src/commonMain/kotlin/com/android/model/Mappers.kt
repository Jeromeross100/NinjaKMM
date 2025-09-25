// shared/src/commonMain/kotlin/com/android/model/Mappers.kt
package com.android.model

import com.android.network.AnimalDto
import com.android.network.TaxonomyDto

fun AnimalDto.toAnimal(): Animal =
    Animal(
        name = name,
        scientificName = scientific_name,
        commonName = common_name,
        phylum = phylum ?: taxonomy?.phylum,
        taxonomy = taxonomy?.let { Taxonomy(it.phylum) },
        slogan = slogan,
        lifespan = lifespan,
        wingspan = wingspan,
        habitat = habitat,
        prey = prey,
        predators = predators
    )
