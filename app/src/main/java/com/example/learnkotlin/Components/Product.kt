package com.example.learnkotlin.Components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.learnkotlin.Page.Products
import com.example.learnkotlin.R

data class Product(
    var id: Int,
    var name: String,
    var price: Double,
    var amount: Int,
    var description: String,
    @DrawableRes var image: Int,
//    var type: String
)

val productCatalog: Map<String, Map<String, List<Product>>> = mapOf(
    "Men Fragrance" to mapOf(
        "Popular" to listOf(
            Product(id = 1,"Valentino", 99.00, 100, "Born in Roma Intense ", R.drawable.popular_1),
            Product(id = 2,"Giorgio Armani", 99.00, 100, "Stronger With You", R.drawable.popular_2),
        ), "New Arrivals" to listOf(
            Product(id = 3,"Dior", 99.00, 100, "Sauvage", R.drawable.new_1),
            Product(id = 4,"Chanel", 99.00, 100, "Bleu de Chanel", R.drawable.new_2),
        ), "All Products" to listOf(
            Product(id = 5,"Yves Saint Laurent", 99.00, 100, "Libre’s", R.drawable.all_1),
            Product(id = 6,"Versace", 99.99, 100, "Eros", R.drawable.all_2),
        )
    ),
    "Women Fragrance" to mapOf(
        "Popular" to listOf(
            Product(id = 7,"Yves Saint Laurent", 150.00, 100, "Libre’s ", R.drawable.popular_3),
            Product(id = 8,"Chanel", 120.00, 100, "Chanel N.5 ", R.drawable.popular_4),
        ), "New Arrivals" to listOf(
            Product(id = 9,"Dior", 150.00, 100, "JOY Dior", R.drawable.new_2),
            Product(id = 10,"GUCCI", 150.00, 100, "Flora G.G", R.drawable.new_4),
        ), "All Products" to listOf(
            Product(id = 11,"Versace", 180.00, 100, "Bright Crystal", R.drawable.all_3),
            Product(id = 12,"Dior", 150.00, 75, "Miss Dior ", R.drawable.all_4),
        )
    ),
    "Eau de Toilette (EDT)" to mapOf(
        "Popular" to listOf(
            Product(id = 13,"Yves Saint Laurent", 150.00, 100, "Marc Jacobs Daisy", R.drawable.popular_5),
            Product(id = 14,"GUCCI", 99.00, 75, "Gucci Bloom", R.drawable.popular_6),
        ), "New Arrivals" to listOf(
            Product(id = 15,"Dior", 150.00, 100, "JOY Dior", R.drawable.new_5),
            Product(id = 16,"", 150.00, 100, "Flora G.G", R.drawable.new_6),
        ), "All Products" to listOf(
            Product(id = 17,"Versace", 180.00, 100, "Bright Crystal", R.drawable.all_5),
            Product(id = 18,"Dior", 150.00, 75, "Miss Dior ", R.drawable.all_6),
        )
    ),
    "Eau de Parfum (EDP)" to mapOf(
        "Popular" to listOf(
            Product(id = 13,"Yves Saint Laurent", 150.00, 100, "", R.drawable.popular_5),
            Product(id = 14,"GUCCI", 99.00, 75, "Gucci Bloom", R.drawable.popular_6),
        ), "New Arrivals" to listOf(
            Product(id = 15,"Dior", 150.00, 100, "JOY Dior", R.drawable.new_2),
            Product(id = 16,"GUCCI", 150.00, 100, "Flora G.G", R.drawable.new_4),
        ), "All Products" to listOf(
            Product(id = 17,"Versace", 180.00, 100, "Bright Crystal", R.drawable.all_1),
            Product(id = 18,"", 150.00, 75, "Miss Dior ", R.drawable.new_2),
        )
    ),
    "Unisex Fragrance" to mapOf(
        "Popular" to listOf(
            Product(id = 13,"Yves Saint Laurent", 150.00, 100, "", R.drawable.popular_5),
            Product(id = 14,"GUCCI", 99.00, 75, "Gucci Bloom", R.drawable.popular_6),
        ), "New Arrivals" to listOf(
            Product(id = 15,"Dior", 150.00, 100, "JOY Dior", R.drawable.new_2),
            Product(id = 16,"GUCCI", 150.00, 100, "Flora G.G", R.drawable.new_4),
        ), "All Products" to listOf(
            Product(id = 17,"Versace", 180.00, 100, "Bright Crystal", R.drawable.all_1),
            Product(id = 18,"", 150.00, 75, "Miss Dior ", R.drawable.new_2),
        )
    )
)

@Composable
fun CategoryScreen(categoryName: String, navController: NavController, cartViewModel: CartViewModel) {
    // 1. Look up the data for the given category (e.g., "Men Fragrance")
    val categoryProducts = productCatalog[categoryName] ?: emptyMap()
    // 2. Get the specific lists from that data
    val popularProducts = categoryProducts["Popular"] ?: emptyList()
    val newArrivals = categoryProducts["New Arrivals"] ?: emptyList()
    val allProducts = categoryProducts["All Products"] ?: emptyList()

    // 3. Display them in a LazyColumn
    LazyColumn {
        if (popularProducts.isNotEmpty()) {
            item { Products(popularProducts, "Popular", navController, cartViewModel) }
        }
        if (newArrivals.isNotEmpty()) {
            item { Products(newArrivals, "New Arrivals", navController, cartViewModel) }
        }
        if (allProducts.isNotEmpty()) {
            item { Products(allProducts, "All Products", navController, cartViewModel) }
        }
    }
}
