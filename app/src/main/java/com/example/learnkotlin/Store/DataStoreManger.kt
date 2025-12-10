package com.example.learnkotlin.Store

import android.content.Context
import androidx.datastore.preferences.core.edit
// FIX 1: Import the correct key type for a SET of strings.
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class DataStoreManger(val context: Context) {
    // FIX 2: Use a key designed for a SET of strings.
    private val favoritesKey = stringSetPreferencesKey("favorite_products_key")

    // FIX 3: Renamed the function for clarity, as it saves multiple favorites.
    suspend fun saveFavorites(productIds: Set<Int>) {
        context.dataStore.edit { preferences ->
            // Now you can correctly assign the Set<String> to the stringSetPreferencesKey.
            val stringSet = productIds.map { it.toString() }.toSet()
            preferences[favoritesKey] = stringSet
        }
    }

    val getFavorites: Flow<Set<Int>> = context.dataStore.data
        .map { preferences ->
            // This will now correctly read the Set<String> or return an empty set if it doesn't exist.
            val stringSet = preferences[favoritesKey] ?: emptySet()
            stringSet.mapNotNull { it.toIntOrNull() }.toSet()
        }
}