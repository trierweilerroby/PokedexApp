package com.example.pokedexapp682474.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp682474.model.Pokemon
import com.example.pokedexapp682474.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var lastAttemptedId: Int? = null

    private val _pokemon = MutableStateFlow<Pokemon?>(null)
    val pokemon: StateFlow<Pokemon?> = _pokemon.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _evolutionChain = MutableStateFlow<List<Pokemon>>(emptyList())
    val evolutionChain: StateFlow<List<Pokemon>> = _evolutionChain.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun loadPokemon(id: Int) {
        lastAttemptedId = id
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val fetchedPokemon = repository.getPokemonDetails(id)
                _pokemon.value = fetchedPokemon
                updateFavoriteState(fetchedPokemon)
                loadEvolution(id)
            } catch (e: Exception) {
                _error.value = "Failed to load Pokémon details"
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun updateFavoriteState(pokemon: Pokemon?) {
        if (pokemon != null) {
            val favorites = repository.getFavorites().first()
            _isFavorite.value = favorites.contains(pokemon.id)
        }
    }

    private fun loadEvolution(id: Int) {
        viewModelScope.launch {
            val chain = repository.getEvolutionChain(id)
            _evolutionChain.value = chain ?: emptyList()
        }
    }

    fun retry() {
        lastAttemptedId?.let { loadPokemon(it) }
    }

    fun toggleFavorite() {
        _pokemon.value?.let { pokemon ->
            viewModelScope.launch {
                repository.toggleFavorite(pokemon)
                updateFavoriteState(pokemon)
            }
        }
    }
}
