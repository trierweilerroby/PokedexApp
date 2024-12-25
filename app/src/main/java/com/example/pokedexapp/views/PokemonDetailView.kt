package com.example.pokedexapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.theme.LightColorScheme
import com.example.pokedexapp.theme.lightCustomColors
import java.util.Locale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.pokedexapp.data.DataStore

@Composable
fun PokemonDetailView(
    pokemon: Pokemon,
    isFavorite: Boolean,
    onToggleFavorite: (Pokemon) -> Unit
) {
    // Add a ScrollState to enable scrolling
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightColorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = pokemon.name.replaceFirstChar { it.titlecase(Locale.ROOT) },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C)
                )
            )

            // favorites
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite Pokémon",
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        onToggleFavorite(pokemon)
                    },
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }

        // Pokemon ID
        Text(
            text = "#${pokemon.id.toString().padStart(3, '0')}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF8E8E8E),
                fontSize = 18.sp
            ),
            modifier = Modifier.align(Alignment.End)
        )

        // Types (Badge-like elements)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            pokemon.types?.forEach { type ->
                TypeBadge(type = type, badgeColor = getTypeColor(type))
            }
        }

        // Pokemon Image
        Image(
            painter = rememberAsyncImagePainter(pokemon.imageUrl),
            contentDescription = "${pokemon.name} image",
            modifier = Modifier
                .size(200.dp) // Larger image size
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        // Tabs (About, Stats, Evolution)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TabItem(text = "About", selected = true)
            TabItem(text = "Stats", selected = false)
            TabItem(text = "Evolution", selected = false)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Information section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
        ) {
            InfoRow(label = "Name", value = pokemon.name.replaceFirstChar { it.titlecase(Locale.ROOT) })
            InfoRow(label = "ID", value = "#${pokemon.id.toString().padStart(3, '0')}")
            InfoRow(label = "Base", value = "${pokemon.baseExperience} XP")
            InfoRow(label = "Weight", value = "${pokemon.weight?.div(10.0)} kg")
            InfoRow(label = "Height", value = "${pokemon.height?.div(10.0)} m")
            InfoRow(label = "Types", value = pokemon.types?.joinToString(", ") ?: "")
            InfoRow(label = "Abilities", value = pokemon.abilities?.joinToString(", ") ?: "")
        }
    }
}


private fun getTypeColor(type: String): Color {
    val customColors = lightCustomColors()
    return when (type.lowercase(Locale.ROOT)) {
        "grass" -> customColors.grassGreen
        "fire" -> customColors.fireRed
        "electric" -> customColors.electricYellow
        else -> Color.Blue
    }
}

@Composable
fun TypeBadge(type: String, badgeColor: Color) {
    Text(
        text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
        modifier = Modifier
            .background(badgeColor, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        color = Color.White,
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    )
}

@Composable
fun TabItem(text: String, selected: Boolean) {
    Text(
        text = text,
        color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF8E8E8E),
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .padding(vertical = 8.dp)
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF2C2C2C),
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF8E8E8E)
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PokemonDetailViewPreview() {
    val samplePokemon = Pokemon(
        id = 25,
        name = "Pikachu",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
        baseExperience = 112,
        weight = 60,
        height = 4,
        types = listOf("Electric"),
        abilities = listOf("Static", "Lightning Rod")
    )

    val isFavorite = remember { mutableStateOf(false) }

    PokemonDetailView(
        pokemon = samplePokemon,
        isFavorite = isFavorite.value,
        onToggleFavorite = {
            isFavorite.value = !isFavorite.value
        }
    )
}
