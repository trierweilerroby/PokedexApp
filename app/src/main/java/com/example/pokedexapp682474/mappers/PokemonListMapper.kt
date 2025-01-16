package com.example.pokedexapp682474.mappers

import com.example.pokedexapp682474.model.Pokemon
import com.example.pokedexapp682474.model.PokemonListResponse
import javax.inject.Inject

class PokemonListMapper @Inject constructor() {
    fun mapPokemonList(response: PokemonListResponse): List<Pokemon> {
        return response.results.map { pokemonResponse ->
            val id = extractPokemonIdFromUrl(pokemonResponse.url)
            Pokemon(
                id = id,
                name = pokemonResponse.name,
                imageUrl = generatePokemonImageUrl(id)
            )
        }
    }

    private fun extractPokemonIdFromUrl(url: String): Int {
        return url.trimEnd('/').substringAfterLast('/').toInt()
    }

    private fun generatePokemonImageUrl(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }
}
