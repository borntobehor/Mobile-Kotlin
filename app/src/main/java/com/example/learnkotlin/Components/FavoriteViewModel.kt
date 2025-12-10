package com.example.learnkotlin.Components

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.learnkotlin.Components.Product
import com.example.learnkotlin.Store.DataStoreManger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.collections.toMutableSet

// This object will hold and manage our list of favorite products.
object FavoriteViewModel {

    private lateinit var dataStore: DataStoreManger
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var favoriteProductId = mutableStateOf<Set<Int>>(emptySet())

    fun initialize(context: Context) {
        dataStore = DataStoreManger(context)

        coroutineScope.launch {
            favoriteProductId.value = dataStore.getFavorites.first()
        }
    }

    // Function to check if a product is already in the list.
    fun isFavorite(product: Product): Boolean {
//        return favorites.contains(product)
        return favoriteProductId.value.contains(product.id)
    }
    // Function to toggle a product's favorite status.
    fun toggleFavorite(product: Product) {
        coroutineScope.launch {
            val currentFavorites = favoriteProductId.value.toMutableSet()
            if(currentFavorites.contains(product.id)) {
                currentFavorites.remove(product.id)
            } else {
                currentFavorites.add(product.id)
            }
            dataStore.saveFavorites(currentFavorites)
            favoriteProductId.value = currentFavorites
        }
    }

    // Add this function to clear the entire list.
    fun clearAllFavorites() {
        coroutineScope.launch {
            // Save an empty set to the DataStore
            dataStore.saveFavorites(emptySet())
            // Update the in-memory state to clear the UI
            favoriteProductId.value = emptySet()
        }
    }
}