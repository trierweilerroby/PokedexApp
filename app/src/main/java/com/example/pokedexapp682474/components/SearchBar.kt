package com.example.pokedexapp682474.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search Pokémon") },
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0x80E0F7FA), shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),
        leadingIcon = {
            Icon(
                imageVector = if (query.isEmpty()) Icons.Filled.Search else Icons.Filled.Clear,
                contentDescription = if (query.isEmpty()) "Search icon" else "Clear icon",
                modifier = Modifier.clickable(enabled = query.isNotEmpty()) {
                    onQueryChange("")
                }
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    val query = remember { mutableStateOf("") }
    SearchBar(
        query = query.value,
        onQueryChange = { query.value = it }
    )
}