package com.example.learnkotlin.Page

import android.R.attr.action
import android.R.attr.minHeight
import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.learnkotlin.Components.BackAppBar
import com.example.learnkotlin.Components.FavoriteViewModel
import com.example.learnkotlin.Components.Product
import com.example.learnkotlin.Components.productCatalog
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text
import com.example.learnkotlin.ui.theme.White
import kotlin.collections.flatten

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePage(navController: NavController) {
    val favoriteProductId = FavoriteViewModel.favoriteProductId.value
    // Flatten the entire catalog, then filter by favorited IDs and ensure unique IDs
    val favoriteProducts = productCatalog.values
        .flatMap { it.values }
        .flatten()
        .filter { product -> favoriteProductId.contains(product.id) }
        .distinctBy { it.id }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Primary,
        topBar = {
            BackAppBar(navController, "Favorites")
        }
    ) { it ->
        if (favoriteProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("You haven't added any favorites yet ! <3", color = White)
            }
        } else {
            LazyVerticalGrid(
                // We want a grid with 2 columns
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                // Add padding around the grid and between items
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // The 'items' extension for grids works just like for LazyColumn
                items(favoriteProducts, key = { it.id }) { product ->
                    // No Box or weights needed. LazyVerticalGrid handles the alignment.
                    ProductCard(product = product, name = "Favorites")
                }
            }
        }
    }
}