package com.example.pokedexapp.model

data class PokemonSpeciesResponse(
    val evolution_chain: EvolutionChainInfo
)

data class EvolutionChainInfo(
    val url: String
)
