package com.example.pokedexapp.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val baseExperience: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val types: List<String>? = null,
    val abilities: List<String>? = null
)