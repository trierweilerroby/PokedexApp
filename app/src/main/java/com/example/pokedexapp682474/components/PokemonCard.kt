package com.example.pokedexapp682474.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pokedexapp682474.model.Pokemon
import java.util.Locale

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    showSnackbar: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .size(width = 181.dp, height = 220.dp)
            .padding(4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(0.5.dp, Color.LightGray),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Row with ID and Favorite Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pokemon.id.toString().padStart(3, '0'),
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(25)
                        )
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 15.sp)
                )

                androidx.compose.material3.Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Unfavorite Pokémon" else "Favorite Pokémon",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.clickable {
                        onToggleFavorite()
                        val message = if (!isFavorite) {

                            "${pokemon.name} added to Favorites"
                        } else {
                            "${pokemon.name} removed from Favorites"
                        }
                        showSnackbar(message)
                    }
                )
            }

            Image(
                painter = rememberAsyncImagePainter(model = pokemon.imageUrl),
                contentDescription = "${pokemon.name} image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )

            Text(
                text = pokemon.name.replaceFirstChar { it.titlecase(Locale.ROOT) },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp, bottom = 6.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
    }
}
