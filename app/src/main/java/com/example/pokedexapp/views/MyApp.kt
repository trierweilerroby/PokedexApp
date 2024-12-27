package com.example.pokedexapp.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedexapp.PokemonApp
import com.example.pokedexapp.components.BottomNav
import com.example.pokedexapp.components.SearchBar
import com.example.pokedexapp.data.DataStore
import com.example.pokedexapp.model.Pokemon

@Composable
fun MyApp(
    dataStore: DataStore = hiltViewModel(),
) {
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
            Modifier.padding(innerPadding)
        ) {
            // Pokémon List Screen
            composable("pokemons") {
                PokemonListScreen(dataStore, navController, searchQuery.value) { query ->
                    searchQuery.value = query
                }
            }

            // Favorites Screen
            composable("favorites") {
                FavoritesView(
                    dataStore = dataStore,
                    navController = navController
                )
            }

            // Pokémon Detail Screen
            composable("pokemonDetail/{pokemonId}") { backStackEntry ->
                val pokemonIdString = backStackEntry.arguments?.getString("pokemonId")
                val pokemonId = pokemonIdString?.toIntOrNull()

                if (pokemonId != null) {
                    val pokemon = remember { mutableStateOf<Pokemon?>(null) }
                    val loading = remember { mutableStateOf(true) }
                    val favoritePokemonList by dataStore.favoritePokemonList.collectAsState()

                    LaunchedEffect(pokemonId) {
                        dataStore.getPokemonDetailsById(pokemonId) { fetchedPokemon ->
                            pokemon.value = fetchedPokemon
                            loading.value = false
                        }
                    }

                    if (loading.value) {
                        Text("Loading...")
                    } else {
                        pokemon.value?.let { fetchedPokemon ->
                            // Dynamically derive `isFavorite` from `DataStore`
                            val isFavorite by dataStore.favoritePokemonList.collectAsState()
                                .let { derivedStateOf { it.value.contains(fetchedPokemon) } }

                            // Pass `onToggleFavorite` to update favorites
                            PokemonDetailView(
                                pokemon = fetchedPokemon,
                                dataStore = dataStore,
                                navController = navController
                            )
                        } ?: run {
                            Text("Pokémon not found")
                        }
                    }
                } else {
                    Text("Invalid Pokémon ID")
                }
            }
        }
    }
}

@Composable
fun PokemonListScreen(
    viewModel: DataStore,
    navController: NavController,
    searchQuery: String,
    onQueryChange: (String) -> Unit
) {
    Column {
        // Add the SearchBar at the top
        SearchBar(
            query = searchQuery,
            onQueryChange = onQueryChange
        )

        // Pass the searchQuery to filter Pokémon in the PokemonListView
        PokemonListView(
            dataStore = viewModel,
            navController = navController,
            searchQuery = searchQuery
        )
    }
}

@Preview
@Composable
fun PreviewPokemonApp() {
    PokemonApp()
}
