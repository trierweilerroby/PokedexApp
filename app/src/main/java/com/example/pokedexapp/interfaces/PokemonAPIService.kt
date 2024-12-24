package com.example.pokedexapp.interfaces

import com.example.pokedexapp.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonAPIService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 1000,
        @Query("offset") offset: Int = 0,
    ): PokemonListResponse
}