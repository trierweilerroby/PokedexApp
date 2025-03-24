package com.example.pokedexapp682474.views

import StatsSection
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp682474.model.Pokemon
import com.example.pokedexapp682474.theme.LightColorScheme
import com.example.pokedexapp682474.theme.lightCustomColors
import java.util.Locale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pokedexapp682474.components.EvolutionSection
import com.example.pokedexapp682474.viewmodels.PokemonDetailViewModel


@Composable
fun PokemonDetailView(
    pokemonId: Int,
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val selectedTab = remember { mutableStateOf("About") }

    // Load Pokémon details
    LaunchedEffect(pokemonId) {
        viewModel.loadPokemon(pokemonId)
    }

    val pokemon by viewModel.pokemon.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val evolutionData by viewModel.evolutionChain.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val context = LocalContext.current

    if (loading) {
        Text("Loading...")
        return
    }

    pokemon?.let { poke ->

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

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = poke.name.replaceFirstChar { it.titlecase(Locale.ROOT) },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C2C2C)
                    )
                )

                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Pokémon",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { sharePokemon(context, poke) },
                    tint = Color.Gray
                )
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite Pokémon",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { viewModel.toggleFavorite() },
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }


            Text(
                text = "#${poke.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF8E8E8E),
                    fontSize = 18.sp
                ),
                modifier = Modifier.align(Alignment.End)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                poke.types?.forEach { type ->
                    TypeBadge(type = type, badgeColor = getTypeColor(type))
                }
            }

            Image(
                painter = rememberAsyncImagePainter(poke.imageUrl),
                contentDescription = "${poke.name} image",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TabItem("About", selectedTab.value == "About") { selectedTab.value = "About" }
                TabItem("Stats", selectedTab.value == "Stats") { selectedTab.value = "Stats" }
                TabItem("Evolution", selectedTab.value == "Evolution") { selectedTab.value = "Evolution" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab.value) {
                "About" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(10.dp),
                    ) {
                        InfoRow("Name", poke.name.replaceFirstChar { it.titlecase(Locale.ROOT) })
                        InfoRow("ID", "#${poke.id.toString().padStart(3, '0')}")
                        InfoRow("Base", "${poke.baseExperience} XP")
                        InfoRow("Weight", "${poke.weight?.div(10.0)} kg")
                        InfoRow("Height", "${poke.height?.div(10.0)} m")
                        InfoRow("Types", poke.types?.joinToString(", ") ?: "")
                        InfoRow("Abilities", poke.abilities?.joinToString(", ") ?: "")
                    }
                }
                "Stats" -> {
                    poke.stats?.let {
                        StatsSection(stats = it)
                    } ?: Text(
                        text = "Stats data not available.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
                "Evolution" -> {
                    if (evolutionData.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EvolutionSection(evolutionData) { selected ->
                                navController.navigate("pokemonDetail/${selected.id}")
                            }
                        }
                    } else {
                        Text(
                            text = "Loading evolution data...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                }
            }
        }

    } ?: Text("Pokémon not found.")
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
fun TabItem(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF8E8E8E),
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clickable { onClick() }
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

// Function to share Pokémon details
fun sharePokemon(context: Context, pokemon: Pokemon) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this Pokémon!\n\nName: ${pokemon.name}\nID: ${pokemon.id}\nImage: ${pokemon.imageUrl}"
        )
        type = "text/plain"
    }
    ContextCompat.startActivity(
        context,
        Intent.createChooser(shareIntent, "Share Pokémon via"),
        null
    )
}



