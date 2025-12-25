package com.example.learnkotlin.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.learnkotlin.api.CatalogRepository
import com.example.learnkotlin.api.GroupedCatalog
import com.example.learnkotlin.api.PerfumeDto
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.White

// Keep data class Product only if other screens reference it; otherwise it can be removed.
data class Product(
    var id: Int,
    var name: String,
    var price: Double,
    var amount: Int,
    var description: String,
    @androidx.annotation.DrawableRes var image: Int,
)

@Composable
fun CategoryScreen(categoryName: String, navController: NavController, cartViewModel: CartViewModel) {
    val repo = remember { CatalogRepository() }
    var popular by remember { mutableStateOf<List<PerfumeDto>>(emptyList()) }
    var newArrivals by remember { mutableStateOf<List<PerfumeDto>>(emptyList()) }
    var allProducts by remember { mutableStateOf<List<PerfumeDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(categoryName) {
        loading = true
        error = null
        try {
            // Map category to filters
            val (gender, concentration) = when (categoryName) {
                "Men Fragrance" -> "men" to null
                "Women Fragrance" -> "women" to null
                "Unisex Fragrance" -> "unisex" to null
                "Eau de Toilette (EDT)" -> null to "EDT"
                "Eau de Parfum (EDP)" -> null to "EDP"
                else -> null to null
            }
            val list = repo.getPerfumesFiltered(gender = gender, concentration = concentration)
            allProducts = list
            popular = list.filter { it.isPopular }
            newArrivals = list.filter { it.isNewArrival }
        } catch (e: Exception) {
            error = e.message ?: "Failed to load products"
        } finally {
            loading = false
        }
    }

    when {
        loading -> {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = White, modifier = Modifier.padding(24.dp))
            }
        }
        error != null -> {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = error ?: "Error", color = White, modifier = Modifier.padding(24.dp))
            }
        }
        else -> {
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (popular.isNotEmpty()) {
                    item { SectionRow(title = "Popular", items = popular, navController = navController, cartViewModel = cartViewModel) }
                }
                if (newArrivals.isNotEmpty()) {
                    item { SectionRow(title = "New Arrivals", items = newArrivals, navController = navController, cartViewModel = cartViewModel) }
                }
                if (allProducts.isNotEmpty()) {
                    item { SectionRow(title = "All Products", items = allProducts, navController = navController, cartViewModel = cartViewModel) }
                }
            }
        }
    }
}

@Composable
private fun SectionRow(title: String, items: List<PerfumeDto>, navController: NavController, cartViewModel: CartViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = title, color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            androidx.compose.material3.TextButton(onClick = { navController.navigate(com.example.learnkotlin.Page.Routes.ALL_PRODUCTS) }) {
                Text(text = "View All", color = White, fontSize = 14.sp)
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items) { p -> PerfumeSmallCard(p, cartViewModel) }
        }
    }
}

private fun mapToProduct(p: PerfumeDto): Product {
    // Map backend item to local Product used by Cart/Favorites. Use a placeholder image.
    return Product(
        id = (p._id ?: p.name).hashCode(),
        name = p.name,
        price = p.price,
        amount = 100,
        description = p.description ?: "",
        image = com.example.learnkotlin.R.drawable.all_1
    )
}

@Composable
private fun PerfumeSmallCard(p: PerfumeDto, cartViewModel: CartViewModel) {
    Card(colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.08f))) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current).data(p.imageUrl).crossfade(true).build(),
                contentDescription = p.name,
            )
            Text(text = p.name, color = White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp))
            Text(text = "$" + String.format("%.2f", p.price), color = White, fontSize = 12.sp)
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val mapped = remember(p._id) { mapToProduct(p) }
                val isFavorite = com.example.learnkotlin.Components.FavoriteViewModel.isFavorite(mapped)
                androidx.compose.material3.Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Primary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { com.example.learnkotlin.Components.FavoriteViewModel.toggleFavorite(mapped) }
                )
                androidx.compose.material3.Button(
                    onClick = { cartViewModel.addToCart(mapped) },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(text = "Add to cart", color = White, fontSize = 12.sp)
                }
            }
        }
    }
}
