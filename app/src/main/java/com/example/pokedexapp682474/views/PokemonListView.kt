package com.example.pokedexapp682474.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokedexapp682474.components.PokemonCard
import com.example.pokedexapp682474.data.DataStore
import kotlinx.coroutines.launch

@Composable
fun PokemonListView(
    dataStore: DataStore = hiltViewModel(),
    navController: NavController,
    searchQuery: String
) {
    val pokemonList by dataStore.pokemonList.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Filter Pokémon based on the search query
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
        // Snackbar Host for displaying messages
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
                            dataStore = dataStore,
                            onClick = {
                                navController.navigate("pokemonDetail/${pokemon.id}")
                            },
                            showSnackbar = { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
