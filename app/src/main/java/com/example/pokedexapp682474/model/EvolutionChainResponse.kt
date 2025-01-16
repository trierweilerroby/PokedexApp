package com.example.pokedexapp682474.model

data class EvolutionChainResponse(
    val chain: EvolutionNode
)

data class EvolutionNode(
    val species: SpeciesInfo,
    val evolves_to: List<EvolutionNode>
)

data class SpeciesInfo(
    val name: String,
    val url: String
)

