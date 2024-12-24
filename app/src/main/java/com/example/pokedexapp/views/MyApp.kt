package com.example.pokedexapp.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedexapp.PokemonApp
import com.example.pokedexapp.components.BottomNav
import com.example.pokedexapp.data.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun MyApp(
    viewModel: DataStore = hiltViewModel(),
    ){
    val navController = rememberNavController()

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
            composable("pokemons") { PokemonListScreen(viewModel, navController) }
            composable("favorites") { FavoritesScreen() }
        }
    }
}

@Composable
fun PokemonListScreen(viewModel: DataStore, navController: NavController) {
    PokemonListView(
        pokemonStore = viewModel,
        navController = navController,
        /*onPokemonClick = { id ->
            navController.navigate("pokemonDetail/$id")
        },*/
    )
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