package com.example.pokedexapp.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pokedexapp.interfaces.PokemonAPIService
import com.example.pokedexapp.mappers.PokemonListMapper
import com.example.pokedexapp.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@HiltViewModel
class DataStore @Inject constructor(
    private val pokemonListMapper: PokemonListMapper

) : ViewModel() {

    private val apiService: PokemonAPIService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonAPIService::class.java)

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    init {
        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        viewModelScope.launch {
            runCatching {
                // Fetch the Pokémon list from the API
                apiService.getPokemonList()
            }.onSuccess { response ->
                // Map the response using the PokemonListMapper
                _pokemonList.value = pokemonListMapper.mapPokemonList(response)
            }.onFailure { throwable ->
                // Log the error if the fetch fails
                Log.e("PokemonStore", "Error fetching Pokémon list", throwable)
            }
        }
    }
}