package com.android.ninjaskmm.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.android.ninjaskmm.Animal
import com.android.ninjaskmm.viewmodel.AnimalsViewModel

@Composable
fun AnimalsScreen(vm: AnimalsViewModel) {
    val s by vm.state.collectAsState()

    val landscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = s.query,
            onValueChange = vm::onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search by name or common name") },
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        when {
            s.loading -> {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            s.error != null -> {
                Text("Error: ${s.error}", color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                FilledTonalButton(onClick = { vm.refresh(force = true) }) { Text("Retry") }
            }
            else -> {
                if (landscape) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(s.filtered) { a -> AnimalCard(a) }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(s.filtered) { a -> AnimalCard(a) }
                    }
                }
            }
        }
    }
}
// ...

private fun formatList(list: List<String>?): String =
    if (list.isNullOrEmpty()) "—" else list.joinToString(", ")

@Composable
private fun AnimalCard(a: Animal) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(a.name ?: "Unknown", style = MaterialTheme.typography.titleMedium)
            Text("Common: ${a.commonName ?: "—"}")
            Text("Phylum: ${a.phylum ?: "—"}")
            Text("Scientific: ${a.scientificName ?: "—"}")

            if (!a.slogan.isNullOrBlank()) Text("Slogan: ${a.slogan}")
            if (!a.lifespan.isNullOrBlank()) Text("Lifespan: ${a.lifespan}")
            if (!a.wingspan.isNullOrBlank()) Text("Wingspan: ${a.wingspan}")
            if (!a.habitat.isNullOrBlank()) Text("Habitat: ${a.habitat}")

            // ✅ Treat as List<String>?
            Text("Prey: ${formatList(a.prey)}")
            Text("Predators: ${formatList(a.predators)}")
        }
    }
}
