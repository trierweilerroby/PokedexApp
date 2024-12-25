package com.example.pokedexapp.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.dataStore
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
import dagger.hilt.android.lifecycle.HiltViewModel

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
            composable("pokemons") {
                PokemonListScreen(dataStore, navController, searchQuery.value) { query ->
                    searchQuery.value = query
                }
            }
            composable("favorites") { FavoritesScreen() }

            composable("pokemonDetail/{pokemonId}") { backStackEntry ->
                val pokemonIdString = backStackEntry.arguments?.getString("pokemonId")
                val pokemonId = pokemonIdString?.toIntOrNull()

                if (pokemonId != null) {
                    // Use remember to avoid unnecessary recompositions
                    val pokemon = remember { mutableStateOf<Pokemon?>(null) }
                    val loading = remember { mutableStateOf(true) }

                    // Collect the favourite Pokémon list as a state
                    val favouritePokemonList by dataStore.favouritePokemonList.collectAsState()

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
                            val isFavourite = favouritePokemonList.contains(fetchedPokemon) // Check if it's in the favorites list
                            PokemonDetailView(
                                pokemon = fetchedPokemon,
                                isFavourite = isFavourite,
                                onToggleFavourite = { selectedPokemon ->
                                    dataStore.toggleFavorite(selectedPokemon)
                                }
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
            pokemonStore = viewModel,
            navController = navController,
            searchQuery = searchQuery
        )
    }
}

@Composable
fun FavoritesScreen() {
    Text(text = "Favorites Screen")
}

@Preview
@Composable
fun PreviewPokemonApp() {
    PokemonApp()
}
