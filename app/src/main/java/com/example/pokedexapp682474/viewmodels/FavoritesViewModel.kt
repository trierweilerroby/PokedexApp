package com.example.pokedexapp682474.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp682474.model.Pokemon
import com.example.pokedexapp682474.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    val favoritePokemonList: StateFlow<Set<Int>> = repository.getFavorites().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    init {
        viewModelScope.launch {
            try {
                _pokemonList.value = repository.getPokemonList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            repository.toggleFavorite(pokemon)
        }
    }
}
