package com.example.pokedexapp682474.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pokedexapp682474.model.Pokemon
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "favorites")

class LocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_pokemon_ids")
    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data
                .map { prefs -> prefs[FAVORITES_KEY]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet() }
                .collect {
                    _favorites.value = it
                }
        }
    }

    fun getFavoriteIds(): Flow<Set<Int>> = _favorites

    suspend fun toggleFavorite(pokemon: Pokemon) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            val idStr = pokemon.id.toString()
            if (idStr in current) {
                current.remove(idStr)
            } else {
                current.add(idStr)
            }
            prefs[FAVORITES_KEY] = current
        }
    }
}
