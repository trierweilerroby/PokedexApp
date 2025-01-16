package com.example.pokedexapp682474.model

data class PokemonSpeciesResponse(
    val evolution_chain: EvolutionChainInfo
)

data class EvolutionChainInfo(
    val url: String
)
