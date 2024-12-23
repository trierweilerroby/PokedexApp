package com.example.pokedexapp.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedexapp.PokemonApp
import com.example.pokedexapp.components.BottomNav

@Composable
fun MyApp(){
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
            composable("pokemons") { PokemonScreen() }
            composable("favorites") { FavoritesScreen() }
        }
    }
}

@Composable
fun PokemonScreen() {
    Text(text = "Pokémon Screen")
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