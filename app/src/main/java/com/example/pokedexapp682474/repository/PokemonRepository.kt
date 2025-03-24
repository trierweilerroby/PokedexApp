package com.example.pokedexapp682474.repository

import com.example.pokedexapp682474.data.LocalDataSource
import com.example.pokedexapp682474.data.RemoteDataSource
import com.example.pokedexapp682474.model.Pokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) {
    suspend fun getPokemonList(): List<Pokemon> = remote.getPokemonList()

    fun getFavorites(): Flow<Set<Int>> = local.getFavoriteIds()

    suspend fun toggleFavorite(pokemon: Pokemon) = local.toggleFavorite(pokemon)

    suspend fun getPokemonDetails(id: Int): Pokemon? = remote.getPokemonDetails(id)

    suspend fun getEvolutionChain(id: Int): List<Pokemon>? = remote.getEvolutionChainForPokemon(id)
}