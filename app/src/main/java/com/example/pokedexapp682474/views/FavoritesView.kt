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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokedexapp682474.components.PokemonCard
import com.example.pokedexapp682474.viewmodels.FavoritesViewModel
import kotlinx.coroutines.launch

@Composable
fun FavoritesView(
    viewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    val allPokemons by viewModel.pokemonList.collectAsState()
    val favoriteIds by viewModel.favoritePokemonList.collectAsState()

    val favoritePokemonList = allPokemons.filter { it.id in favoriteIds }


    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(start = 10.dp, top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Favourite Pokémon's",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 6.dp)
        )

        SnackbarHost(hostState = snackbarHostState)

        if (favoritePokemonList.isEmpty()) {
            Text(
                text = "No favorite Pokémon found.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favoritePokemonList.chunked(2)) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        row.forEach { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                isFavorite = true,
                                onClick = { navController.navigate("pokemonDetail/${pokemon.id}") },
                                onToggleFavorite = { viewModel.toggleFavorite(pokemon) },
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
}
