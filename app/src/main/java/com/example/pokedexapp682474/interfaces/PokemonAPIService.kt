package com.example.pokedexapp682474.interfaces

import com.example.pokedexapp682474.model.EvolutionChainResponse
import com.example.pokedexapp682474.model.PokemonDetailsResponse
import com.example.pokedexapp682474.model.PokemonListResponse
import com.example.pokedexapp682474.model.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPIService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 1000,
        @Query("offset") offset: Int = 0,
    ): PokemonListResponse

    // Method for fetching Pokémon details by ID
    @GET("pokemon/{id}")
    suspend fun getPokemonDetailsById(@Path("id") id: Int): PokemonDetailsResponse

    // Endpoint to fetch Pokémon species (needed to fetch evolution chain URL)
    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpeciesResponse

    // Endpoint to fetch evolution chain data
    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") id: Int): EvolutionChainResponse
}