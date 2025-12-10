package com.example.learnkotlin.Components

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
// FIX 2: Import AndroidViewModel instead of ViewModel
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

// Data class to represent an item in the cart
data class CartItem(val product: Product, val quantity: Int)

// FIX 2: Extend AndroidViewModel to get access to the application context
class CartViewModel(application: Application) : AndroidViewModel(application) {

    // Define constants for SharedPreferences to avoid typos
    companion object {
        private const val PREFS_NAME = "CartPrefs"
        private const val CART_ITEMS_KEY = "CartItems"
    }

    private val _cartItems = mutableStateMapOf<Int, CartItem>()
    val cartItems: Map<Int, CartItem> = _cartItems

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice = _totalPrice.asStateFlow()

    init {
        // FIX 5: Load the cart from storage when the ViewModel is first created
        loadCart()
    }

    private fun loadCart() {
        val prefs =
            getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(CART_ITEMS_KEY, null)

        if (jsonString != null) {
            val jsonObject = JSONObject(jsonString)
            // Get a flat list of all products from your catalog to find items by ID
            val allProducts = productCatalog.values.flatMap { it.values }.flatten()

            jsonObject.keys().forEach { idString ->
                val id = idString.toInt()
                val quantity = jsonObject.getInt(idString)
                // Find the full product object from the catalog
                val product = allProducts.find { it.id == id }
                if (product != null) {
                    _cartItems[id] = CartItem(product, quantity)
                }
            }
        }
        // Update the total price after loading
        updateTotalPrice()
    }

    private fun saveCart() {
        // FIX 3: Get context via getApplication() provided by AndroidViewModel
        val prefs =
            getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonObject = JSONObject()
        _cartItems.forEach { (id, cartItem) ->
            jsonObject.put(id.toString(), cartItem.quantity)
        }
        prefs.edit().putString(CART_ITEMS_KEY, jsonObject.toString()).apply()
    }

    private fun updateAndSave() {
        updateTotalPrice()
        saveCart()
    }

    fun addToCart(product: Product) {
        val existingItem = _cartItems[product.id]
        if (existingItem != null) {
            _cartItems[product.id] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            _cartItems[product.id] = CartItem(product = product, quantity = 1)
        }
        updateAndSave()
    }

    fun removeFromCart(product: Product) {
        _cartItems.remove(product.id)
        updateAndSave()
    }

    fun increaseQuantity(product: Product) {
        val item = _cartItems[product.id]
        if (item != null) {
            _cartItems[product.id] = item.copy(quantity = item.quantity + 1)
            updateAndSave()
        }
    }

    fun decreaseQuantity(product: Product) {
        val item = _cartItems[product.id]
        if (item != null) {
            if (item.quantity > 1) {
                _cartItems[product.id] = item.copy(quantity = item.quantity - 1)
                // FIX 4: Ensure save is called in all modification paths
                updateAndSave()
            } else {
                // If quantity is 1, decrease means removing the item
                removeFromCart(product)
            }
        }
    }

    // Clear all items from the cart and persist the empty state
    fun clearCart() {
        _cartItems.clear()
        updateAndSave()
    }

    private fun updateTotalPrice() {
        _totalPrice.update {
            _cartItems.values.sumOf { it.product.price * it.quantity }
        }
    }
}