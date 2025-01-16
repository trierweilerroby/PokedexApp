package com.example.pokedexapp682474.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNav(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false, // Add logic to track selection
            onClick = { navController.navigate("pokemons") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Pokemons") },
            label = { Text("Pokémons") }
        )
        NavigationBarItem(
            selected = false, // Add logic to track selection
            onClick = { navController.navigate("favorites") },
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorites") },
            label = { Text("Favorites") }
        )
    }
}
