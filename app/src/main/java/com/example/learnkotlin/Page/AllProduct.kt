package com.example.learnkotlin.Page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.learnkotlin.Components.BackAppBar
import com.example.learnkotlin.api.CatalogViewModel
import com.example.learnkotlin.api.PerfumeDto
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProduct(navController: NavController, cartViewModel: com.example.learnkotlin.Components.CartViewModel, catalogViewModel: CatalogViewModel = viewModel()) {
    val items by catalogViewModel.items.collectAsState()
    val loading by catalogViewModel.loading.collectAsState()
    val error by catalogViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        catalogViewModel.load()
    }

    Scaffold(
        containerColor = Primary,
        topBar = {
            BackAppBar(navController, "All Products")
        }
    ) { padding ->
        when {
            loading -> {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = White)
                }
            }
            error != null -> {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error ?: "Error", color = White)
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(items) { perfume ->
                        PerfumeCard(perfume, cartViewModel)
                    }
                }
            }
        }
    }
}

private fun mapToProduct(p: PerfumeDto): com.example.learnkotlin.Components.Product {
    return com.example.learnkotlin.Components.Product(
        id = (p._id ?: p.name).hashCode(),
        name = p.name,
        price = p.price,
        amount = 100,
        description = p.description ?: "",
        image = com.example.learnkotlin.R.drawable.all_1
    )
}

@Composable
private fun PerfumeCard(perfume: PerfumeDto, cartViewModel: com.example.learnkotlin.Components.CartViewModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.08f))
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(perfume.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = perfume.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(6.dp))
            Text(text = perfume.name, color = White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            val priceText = "$" + String.format("%.2f", perfume.price)
            Text(text = priceText, color = White, fontSize = 12.sp)
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val mapped = remember(perfume._id) { mapToProduct(perfume) }
                val isFavorite = com.example.learnkotlin.Components.FavoriteViewModel.isFavorite(mapped)
                androidx.compose.material3.Icon(
                    imageVector = if (isFavorite) androidx.compose.material.icons.Icons.Filled.Favorite else androidx.compose.material.icons.Icons.Outlined.FavoriteBorder,
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