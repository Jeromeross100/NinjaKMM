// shared/src/commonMain/kotlin/com/android/repository/RepositoryFactory.kt
package com.android.repository

import com.android.network.createHttpClient
import com.russhwolf.settings.Settings

// Aliases to avoid name clashes and to make mapping explicit
import com.android.ninjaskmm.Animal as SharedAnimal
import com.android.ninjaskmm.Taxonomy as SharedTaxonomy
import com.android.network.AnimalsApi as NetAnimalsApi
import com.android.network.AnimalDto as NetAnimalDto
import com.android.network.TaxonomyDto as NetTaxonomyDto

/**
 * Factory that creates an AnimalsRepository for the app to consume.
 * Keeps androidApp unaware of Ktor/HttpClient details.
 */
fun createAnimalsRepository(settings: Settings, apiKey: String): AnimalsRepository {
    val client = createHttpClient(apiKey)
    val netApi = NetAnimalsApi(client)

    // Adapter: implement the repository's API interface by delegating to the network API,
    // and map DTOs -> shared model.
    val repoApi: AnimalsApi = object : AnimalsApi {
        override suspend fun fetchByName(name: String): List<SharedAnimal> {
            val dtos: List<NetAnimalDto> = netApi.fetchByName(name)  // <-- fixed
            return dtos.map { it.toShared() }
        }
    }

    return AnimalsRepository(api = repoApi, settings = settings)
}

/* -------------------- Mappers (DTO -> shared model) -------------------- */

private fun NetAnimalDto.toShared(): SharedAnimal = SharedAnimal(
    name = name.orEmpty(),
    taxonomy = taxonomy.toShared(),
    scientificName = scientific_name.orEmpty(),
    commonName = common_name.orEmpty(),
    phylum = phylum.orEmpty(),
    slogan = slogan.orEmpty(),
    lifespan = lifespan.orEmpty(),
    wingspan = wingspan.orEmpty(),
    habitat = habitat.orEmpty(),
    prey = prey ?: emptyList(),
    predators = predators ?: emptyList()
)

private fun NetTaxonomyDto?.toShared(): SharedTaxonomy? =
    this?.let { SharedTaxonomy(phylum = it.phylum.orEmpty()) }
