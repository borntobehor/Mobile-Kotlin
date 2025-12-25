package com.example.learnkotlin.Store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class DataStoreManger(val context: Context) {
    // Favorites storage
    private val favoritesKey = stringSetPreferencesKey("favorite_products_key")

    // Auth storage
    private val authTokenKey = stringPreferencesKey("auth_token")
    private val authExpiryEpochSecondsKey = stringPreferencesKey("auth_expiry_epoch_seconds")

    // Favorites API
    suspend fun saveFavorites(productIds: Set<Int>) {
        context.dataStore.edit { preferences ->
            val stringSet = productIds.map { it.toString() }.toSet()
            preferences[favoritesKey] = stringSet
        }
    }

    val getFavorites: Flow<Set<Int>> = context.dataStore.data
        .map { preferences ->
            val stringSet = preferences[favoritesKey] ?: emptySet()
            stringSet.mapNotNull { it.toIntOrNull() }.toSet()
        }

    // Auth API
    suspend fun saveAuth(token: String, expiryEpochSeconds: Long?) {
        context.dataStore.edit { preferences ->
            preferences[authTokenKey] = token
            if (expiryEpochSeconds != null) {
                preferences[authExpiryEpochSecondsKey] = expiryEpochSeconds.toString()
            } else {
                preferences.remove(authExpiryEpochSecondsKey)
            }
        }
    }

    val authToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[authTokenKey]
    }

    val authExpiryEpochSeconds: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[authExpiryEpochSecondsKey]?.toLongOrNull()
    }

    suspend fun clearAuth() {
        context.dataStore.edit { prefs ->
            prefs.remove(authTokenKey)
            prefs.remove(authExpiryEpochSecondsKey)
        }
    }
}