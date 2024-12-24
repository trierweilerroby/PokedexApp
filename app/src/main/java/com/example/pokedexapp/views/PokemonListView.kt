package com.example.pokedexapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokedexapp.components.PokemonCard
import com.example.pokedexapp.data.DataStore
import kotlinx.coroutines.launch

@Composable
fun PokemonListView(
    pokemonStore: DataStore = hiltViewModel(),
    navController: NavController,
    searchQuery: String
) {
    val pokemonList by pokemonStore.pokemonList.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val filteredPokemonList = if (searchQuery.isNotEmpty()) {
        pokemonList.filter { pokemon ->
            pokemon.name.contains(searchQuery, ignoreCase = true)
        }
    } else {
        pokemonList
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Snackbar Host
        SnackbarHost(hostState = snackbarHostState)

        // Title
        Text(
            text = "All Pokémon's",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 6.dp)
        )

        // Pokémon List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(filteredPokemonList.chunked(2)) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    row.forEach { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            isFavorite = pokemonStore.isFavorite(pokemon),
                            onFavoriteToggle = {
                                pokemonStore.toggleFavorite(pokemon)
                            },
                            showSnackbar = { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            },
                            onClick = {
                                navController.navigate("pokemonDetail/${pokemon.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
