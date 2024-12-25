package com.example.pokedexapp.model

data class PokemonDetailsResponse(
    val name: String,
    val base_experience: Int,
    val weight: Int,
    val height: Int,
    val types: List<Type>,
    val abilities: List<Ability>
)

data class Type(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

data class Ability(
    val ability: AbilityInfo
)

data class AbilityInfo(
    val name: String
)