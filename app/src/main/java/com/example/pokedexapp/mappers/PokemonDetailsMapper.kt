package com.example.pokedexapp.mappers

import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.model.PokemonDetailsResponse
import javax.inject.Inject

class PokemonDetailsMapper @Inject constructor() {
    fun mapPokemonDetails(id: Int, response: PokemonDetailsResponse): Pokemon {
        return Pokemon(
            id = id,
            name = response.name,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png", // Basic sprite image URL
            baseExperience = response.base_experience,
            height = response.height,
            weight = response.weight,
            types = response.types.map { it.type.name }, // Map type names
            abilities = response.abilities.map { it.ability.name } // Map ability names
        )
    }
}