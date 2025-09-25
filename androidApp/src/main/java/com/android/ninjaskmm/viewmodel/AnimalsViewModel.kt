package com.android.ninjaskmm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.repository.AnimalsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Use the shared model only
import com.android.ninjaskmm.Animal as SharedAnimal

data class AnimalsUiState(
    val query: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val all: List<SharedAnimal> = emptyList()
) {
    val filtered: List<SharedAnimal> =
        if (query.isBlank()) all
        else all.filter { a ->
            (a.name?.contains(query, ignoreCase = true) == true) ||
                    (a.commonName?.contains(query, ignoreCase = true) == true)
        }
}

class AnimalsViewModel(
    private val repo: AnimalsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AnimalsUiState(loading = true))
    val state: StateFlow<AnimalsUiState> = _state.asStateFlow()

    init {
        refresh(force = false)
    }

    fun onQueryChanged(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
    }

    @Deprecated("Use onQueryChanged(newQuery) instead", ReplaceWith("onQueryChanged(q)"))
    fun onQueryChange(q: String) = onQueryChanged(q)

    fun refresh(force: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { repo.getAnimals(force) } // returns List<SharedAnimal>
                .onSuccess { list ->
                    _state.update { it.copy(loading = false, all = list) }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(loading = false, error = e.message ?: "Unknown error")
                    }
                }
        }
    }
}
