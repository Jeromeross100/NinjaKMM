package com.android.ninjaskmm.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.android.ninjaskmm.app.AppLocator
import com.android.ninjaskmm.viewmodel.AnimalsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = AppLocator.animalsRepository(this)
        val vm = AnimalsViewModel(repo)

        setContent { MaterialTheme { AnimalsApp(viewModel = vm) } }
    }
}

@Composable
fun AnimalsApp(viewModel: AnimalsViewModel) {
    val ui by viewModel.state.collectAsState()
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = ui.query,
            onValueChange = { newQuery -> viewModel.onQueryChanged(newQuery) }, // <-- fixed
            label = { Text("Search by name or common name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        when {
            ui.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            ui.error != null -> Text(
                "Error: ${ui.error}",
                color = MaterialTheme.colorScheme.error
            )
            else -> {
                if (isLandscape) {
                    LazyRow(Modifier.fillMaxSize()) {
                        items(ui.filtered) { animal ->
                            AnimalCard(
                                animal = animal,
                                modifier = Modifier.padding(8.dp).width(320.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(ui.filtered) { animal ->
                            AnimalCard(
                                animal = animal,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalCard(
    animal: com.android.ninjaskmm.Animal,
    modifier: Modifier = Modifier
) {
    Card(modifier, elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(animal.name ?: "—", style = MaterialTheme.typography.titleMedium)
            Text(
                "Phylum: ${animal.phylum ?: animal.taxonomy?.phylum ?: "—"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Scientific: ${animal.scientificName ?: "—"}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))

            val n = (animal.name ?: "").lowercase()
            val c = (animal.commonName ?: "").lowercase()
            when {
                "dog" in n || "dog" in c -> {
                    Text("Slogan: ${animal.slogan ?: "—"}")
                    Text("Lifespan: ${animal.lifespan ?: "—"}")
                }
                "bird" in n || "bird" in c -> {
                    Text("Wingspan: ${animal.wingspan ?: "—"}")
                    Text("Habitat: ${animal.habitat ?: "—"}")
                }
                else -> {
                    Text("Prey: ${animal.prey?.joinToString() ?: "—"}")
                    Text("Predators: ${animal.predators?.joinToString() ?: "—"}")
                }
            }
        }
    }
}
