package com.example.learnkotlin.Page

import android.R.attr.navigationIcon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learnkotlin.Components.BackAppBar
import com.example.learnkotlin.Components.productCatalog
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.White

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProduct(navController: NavController) {
    val allProductList = productCatalog.values.flatMap { it.values }.flatten().distinct()
    Scaffold(
        containerColor = Primary,
        topBar = {
            BackAppBar(navController, "All Products")
        }
    ) { it ->
        LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp) // Adds space between items
        ) {
            items(allProductList) {product ->
                ProductCard(product, name = "All Products")
            }
        }

    }
}