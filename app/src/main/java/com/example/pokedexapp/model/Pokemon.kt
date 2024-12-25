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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pokemon) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}