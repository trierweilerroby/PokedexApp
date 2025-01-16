package com.example.pokedexapp682474.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val baseExperience: Int? = null,
    val height: Int? = null, // Decimeters
    val weight: Int? = null, // Hectograms
    val types: List<String>? = null,
    val abilities: List<String>? = null,
    val stats: Map<String, Int>? = null // Key: Stat name (e.g., HP), Value: Base stat
) {

    // Convert height to meters
    fun getHeightInMeters(): String {
        return if (height != null) {
            "${height / 10.0} m"
        } else {
            "Unknown"
        }
    }

    // Convert weight to kilograms
    fun getWeightInKilograms(): String {
        return if (weight != null) {
            "${weight / 10.0} kg"
        } else {
            "Unknown"
        }
    }

    // Override equals to compare Pokémon based on ID
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pokemon) return false
        return id == other.id
    }

    // Override hashCode based on ID
    override fun hashCode(): Int {
        return id.hashCode()
    }

    // Return stats as a formatted string for debugging
    fun getStatsAsString(): String {
        return stats?.entries?.joinToString(separator = "\n") { "${it.key}: ${it.value}" } ?: "No stats available"
    }
}
