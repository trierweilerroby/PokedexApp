package com.example.pokedexapp682474.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokedexapp682474.components.AnimatedSnackbarHost
import com.example.pokedexapp682474.components.PokemonCard
import com.example.pokedexapp682474.viewmodels.PokemonListViewModel
import kotlinx.coroutines.launch

@Composable
fun PokemonListView(
    viewModel: PokemonListViewModel = hiltViewModel(),
    navController: NavController,
    searchQuery: String
) {
    val pokemonList by viewModel.pokemonList.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(error) {
        error?.let {
            val result = snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.retry()
            }
        }
    }



    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.material3.CircularProgressIndicator()
        }
        return
    }


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
        AnimatedSnackbarHost(hostState = snackbarHostState)

        Text(
            text = "All Pokémon's",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 10.dp, bottom = 6.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = filteredPokemonList,
                key = { it.id }
            ) { pokemon ->
                PokemonCard(
                    pokemon = pokemon,
                    isFavorite = favorites.contains(pokemon.id),
                    onClick = { navController.navigate("pokemonDetail/${pokemon.id}") },
                    onToggleFavorite = { viewModel.toggleFavorite(pokemon) },
                    showSnackbar = { message ->
                        coroutineScope.launch { snackbarHostState.showSnackbar(message) }
                    }
                )
            }
        }

    }
}