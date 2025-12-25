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

    // Single-product "Buy Now" flow holder
    private val _buyNowProduct = MutableStateFlow<Product?>(null)
    val buyNowProduct = _buyNowProduct.asStateFlow()

    init {
        loadCart()
    }

    fun setBuyNowProduct(product: Product) {
        _buyNowProduct.value = product
        if (_cartItems[product.id] == null) {
            _cartItems[product.id] = CartItem(product, 1)
            updateAndSave()
        }
    }

    fun clearBuyNowProduct() {
        _buyNowProduct.value = null
    }

    private fun loadCart() {
        // Static catalog was removed. To avoid crashes while we transition the cart to API models,
        // we no longer try to rebuild Product objects from stored IDs. For now, we clear the cart
        // on app start if old data exists.
        val prefs =
            getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(CART_ITEMS_KEY, null)
        if (jsonString != null) {
            // Previous format was a map of id -> quantity. We cannot resolve these to Product now.
            // Clear legacy data to keep app stable.
            prefs.edit().remove(CART_ITEMS_KEY).apply()
        }
        _cartItems.clear()
        updateTotalPrice()
    }

    private fun saveCart() {
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
        } else {
            addToCart(product)
        }
        updateAndSave()
    }

    fun decreaseQuantity(product: Product) {
        val item = _cartItems[product.id]
        if (item != null) {
            if (item.quantity > 1) {
                _cartItems[product.id] = item.copy(quantity = item.quantity - 1)
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