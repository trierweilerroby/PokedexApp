package com.example.pokedexapp.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.interfaces.PokemonAPIService
import com.example.pokedexapp.mappers.PokemonDetailsMapper
import com.example.pokedexapp.mappers.PokemonListMapper
import com.example.pokedexapp.model.EvolutionNode
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


    private val apiService: PokemonAPIService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonAPIService::class.java)

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    private val _favoritePokemonList = MutableStateFlow<Set<Pokemon>>(emptySet())
    val favoritePokemonList: StateFlow<Set<Pokemon>> = _favoritePokemonList


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


    fun getEvolutionChainForPokemon(id: Int, onResult: (List<Pokemon>?) -> Unit) {
        viewModelScope.launch {
            try {
                // Step 1: Fetch Pokémon species details
                val speciesResponse = apiService.getPokemonSpecies(id)


                // Step 2: Fetch evolution chain using the species' evolution chain URL
                val evolutionChainUrl = speciesResponse.evolution_chain.url
                val evolutionChainId = extractIdFromUrl(evolutionChainUrl)
                val evolutionResponse = apiService.getEvolutionChain(evolutionChainId)

                // Step 3: Parse the evolution chain to get Pokémon details
                val evolutions = parseEvolutionChain(evolutionResponse.chain)


                // Pass the parsed evolution chain Pokémon details
                onResult(evolutions)
            } catch (e: Exception) {
                Log.e("DataStore", "Error fetching evolution chain for Pokémon ID $id", e)
                onResult(null)
            }
        }
    }

    private fun parseEvolutionChain(node: EvolutionNode?): List<Pokemon> {
        if (node == null) return emptyList()

        val result = mutableListOf<Pokemon>()
        var current: EvolutionNode? = node
        while (current != null) {
            val id = extractIdFromUrl(current.species.url)
            if (id != -1) {
                result.add(
                    Pokemon(
                        id = id,
                        name = current.species.name,
                        imageUrl = generatePokemonImageUrl(id)
                    )
                )
            }
            current = current.evolves_to.firstOrNull()
        }
        return result
    }


    private fun extractIdFromUrl(url: String): Int {
        return try {
            url.split("/").dropLast(1).last().toInt()
        } catch (e: Exception) {
            Log.e("DataStore", "Error extracting ID from URL: $url", e)
            -1
        }
    }

    private fun generatePokemonImageUrl(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }


}
