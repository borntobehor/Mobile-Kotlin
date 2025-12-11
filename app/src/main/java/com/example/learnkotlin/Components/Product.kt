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
            Product(id = 19,"Coach", 150.00, 100, "Miss Dior Blooming Bouquet", R.drawable.popular_7),
            Product(id = 20,"Lattafa", 99.00, 75, "Yara", R.drawable.popular_8),
        ), "New Arrivals" to listOf(
            Product(id = 21,"Prada", 150.00, 100, "Paradoxe 'Virtual Flower'", R.drawable.new_7),
            Product(id = 22,"Maison Alhambra", 150.00, 100, "Salvo Eau De Parfum", R.drawable.new_8),
        ), "All Products" to listOf(
            Product(id = 23,"Gucci", 180.00, 100, "Gucci Guilty Pour Femme (Eau De Parfum)", R.drawable.all_7),
            Product(id = 24,"Marc Jacobs", 150.00, 75, "Daisy Eau De Toilette", R.drawable.all_8),
        )
    ),
    "Unisex Fragrance" to mapOf(
        "Popular" to listOf(
            Product(id = 25,"Coach", 150.00, 100, "Coach Blue Eau De Toilette", R.drawable.popular_9),
            Product(id = 26,"GUCCI", 99.00, 75, "Gucci Bloom", R.drawable.popular_10),
        ), "New Arrivals" to listOf(
            Product(id = 27,"Notez", 150.00, 100, "Sakura Perfume", R.drawable.new_9),
            Product(id = 28,"Moschino", 150.00, 100, "Toy Boy (Eau De Parfum)", R.drawable.new_9),
        ), "All Products" to listOf(
            Product(id = 29,"Carolina Herrera", 180.00, 100, "Good Girl Very Glam Parfum", R.drawable.all_9),
            Product(id = 30,"Carolina Herrera", 150.00, 75, "Bad Boy Extreme Eau De Parfum", R.drawable.all_10),
        )
    ),
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
