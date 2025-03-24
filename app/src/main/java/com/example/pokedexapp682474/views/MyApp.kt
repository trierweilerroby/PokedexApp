package com.example.pokedexapp682474.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedexapp682474.PokemonApp
import com.example.pokedexapp682474.components.BottomNav
import com.example.pokedexapp682474.components.SearchBar


@Composable
fun MyApp() {
    val navController = rememberNavController()
    val searchQuery = remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNav(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "pokemons",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pokémon List Screen
            composable("pokemons") {
                Column {
                    SearchBar(
                        query = searchQuery.value,
                        onQueryChange = { query -> searchQuery.value = query }
                    )
                    PokemonListView(
                        navController = navController,
                        searchQuery = searchQuery.value
                    )
                }
            }


            // Favorites Screen
            composable("favorites") {
                FavoritesView(navController = navController)
            }

            // Pokémon Detail Screen
            composable("pokemonDetail/{pokemonId}") { backStackEntry ->
                val pokemonIdString = backStackEntry.arguments?.getString("pokemonId")
                val pokemonId = pokemonIdString?.toIntOrNull()

                if (pokemonId != null) {
                    PokemonDetailView(
                        pokemonId = pokemonId,
                        navController = navController
                    )
                } else {
                    Text("Invalid Pokémon ID")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPokemonApp() {
    PokemonApp()
}
