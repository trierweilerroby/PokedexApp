package com.example.pokedexapp682474.data

import android.util.Log
import com.example.pokedexapp682474.interfaces.PokemonAPIService
import com.example.pokedexapp682474.mappers.PokemonDetailsMapper
import com.example.pokedexapp682474.mappers.PokemonListMapper
import com.example.pokedexapp682474.model.EvolutionNode
import com.example.pokedexapp682474.model.Pokemon
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: PokemonAPIService,
    private val listMapper: PokemonListMapper,
    private val detailsMapper: PokemonDetailsMapper
) {
    suspend fun getPokemonList(): List<Pokemon> {
        val response = apiService.getPokemonList()
        return listMapper.mapPokemonList(response)
    }

    suspend fun getPokemonDetails(id: Int): Pokemon? {
        return try {
            val response = apiService.getPokemonDetailsById(id)
            detailsMapper.mapPokemonDetails(id, response)
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Failed to fetch details", e)
            null
        }
    }

    suspend fun getEvolutionChainForPokemon(id: Int): List<Pokemon>? {
        return try {
            val species = apiService.getPokemonSpecies(id)
            val chainId = species.evolution_chain.url.split("/").dropLast(1).last().toInt()
            val chain = apiService.getEvolutionChain(chainId)
            parseEvolutionChain(chain.chain)
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching evolution", e)
            null
        }
    }

    private fun parseEvolutionChain(node: EvolutionNode?): List<Pokemon> {
        val result = mutableListOf<Pokemon>()
        var current = node
        while (current != null) {
            val id = extractIdFromUrl(current.species.url)
            result.add(
                Pokemon(
                    id = id,
                    name = current.species.name,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
                )
            )
            current = current.evolves_to.firstOrNull()
        }
        return result
    }

    private fun extractIdFromUrl(url: String): Int {
        return url.split("/").dropLast(1).last().toIntOrNull() ?: -1
    }
}