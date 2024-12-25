package com.example.pokedexapp.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.interfaces.PokemonAPIService
import com.example.pokedexapp.mappers.PokemonDetailsMapper
import com.example.pokedexapp.mappers.PokemonListMapper
import com.example.pokedexapp.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class DataStore @Inject constructor(
    private val pokemonListMapper: PokemonListMapper,
    private val pokemonDetailsMapper: PokemonDetailsMapper
) : ViewModel() {

    private val _favoritePokemonList = MutableStateFlow<Set<Pokemon>>(emptySet())
    val favoritePokemonList: StateFlow<Set<Pokemon>> = _favoritePokemonList


    private val apiService: PokemonAPIService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonAPIService::class.java)

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    // Favorites storage
    private val _favoritePokemonIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoritePokemonIds: StateFlow<Set<Int>> = _favoritePokemonIds

    init {
        fetchPokemonList()
    }

    // Fetch the Pokémon list from the API
    private fun fetchPokemonList() {
        viewModelScope.launch {
            runCatching {
                apiService.getPokemonList()
            }.onSuccess { response ->
                _pokemonList.value = pokemonListMapper.mapPokemonList(response)
            }.onFailure { throwable ->
                Log.e("DataStore", "Error fetching Pokémon list", throwable)
            }
        }
    }

    // Check if a Pokémon is a favorite
    fun isFavorite(pokemon: Pokemon): Boolean {
        return _favoritePokemonList.value.contains(pokemon)
    }

    // Toggle the favorite status of a Pokémon
    fun toggleFavorite(selectedPokemon: Pokemon) {
        _favoritePokemonList.update { currentFavorites ->
            val updatedFavorites = if (currentFavorites.contains(selectedPokemon)) {
                Log.d("Favorites", "Removing: ${selectedPokemon.name} (ID: ${selectedPokemon.id})")
                currentFavorites - selectedPokemon
            } else {
                Log.d("Favorites", "Adding: ${selectedPokemon.name} (ID: ${selectedPokemon.id})")
                currentFavorites + selectedPokemon
            }
            Log.d("Favorites", "Updated Favorites List: $updatedFavorites")
            Log.d("favouritePokemonList", "$_favoritePokemonList")
            updatedFavorites

        }
    }

    // Method to fetch Pokémon details using the details mapper
    fun getPokemonDetailsById(id: Int, onResult: (Pokemon?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getPokemonDetailsById(id)
                val pokemon = response.let { pokemonDetailsMapper.mapPokemonDetails(id, it) }
                onResult(pokemon)
            } catch (e: Exception) {
                Log.e("PokemonStore", "Error fetching Pokémon details: $e")
                onResult(null)
            }
        }
    }
}
