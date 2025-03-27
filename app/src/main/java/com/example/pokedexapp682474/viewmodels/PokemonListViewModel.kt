package com.example.pokedexapp682474.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp682474.model.Pokemon
import com.example.pokedexapp682474.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList.asStateFlow()

    val favorites: StateFlow<Set<Int>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var lastFetchAttempt: (() -> Unit)? = null

    init {
        fetchPokemonList()
    }

    fun fetchPokemonList() {
        lastFetchAttempt = { fetchPokemonList() }
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _pokemonList.value = repository.getPokemonList()
            } catch (e: Exception) {
                _error.value = "Failed to load Pokémon: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            repository.toggleFavorite(pokemon)
        }
    }

    fun retry() {
        lastFetchAttempt?.invoke()
    }
}