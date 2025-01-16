package com.example.pokedexapp682474.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.pokedexapp682474.model.Pokemon
import java.util.Locale


@Composable
fun EvolutionSection(evolutions: List<Pokemon>, onPokemonClick: (Pokemon) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Evolution Chain",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        evolutions.forEachIndexed { index, pokemon ->
            // Evolution Card
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(width = 160.dp, height = 160.dp)
                    .clickable { onPokemonClick(pokemon) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(pokemon.imageUrl),
                            contentDescription = "${pokemon.name} image",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp)
                        )
                        Text(
                            text = pokemon.name.replaceFirstChar { it.titlecase(Locale.ROOT) },
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Arrow Between Evolutions
            if (index < evolutions.size - 1) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Arrow to next evolution",
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
