package com.example.learnkotlin.Page


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnkotlin.api.CatalogRepository
import com.example.learnkotlin.api.PerfumeDto
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text
import com.example.learnkotlin.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onCloseClicked: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    val repo = remember { CatalogRepository() }
    var allProducts by remember { mutableStateOf<List<PerfumeDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        loading = true
        error = null
        try {
            allProducts = repo.getPerfumes()
        } catch (e: Exception) {
            error = e.message ?: "Failed to load products"
        } finally {
            loading = false
        }
    }

    // Filter products based on the current query
    val searchResults: List<PerfumeDto> = remember(searchQuery, allProducts) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            allProducts.filter { p ->
                p.name.contains(searchQuery, ignoreCase = true) ||
                        (p.description ?: "").contains(searchQuery, ignoreCase = true) ||
                        (p.brand ?: "").contains(searchQuery, ignoreCase = true)
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Search",
                        fontWeight = FontWeight.Bold,
                        color = Text
                    )
                },
                navigationIcon = { /* No navigation icon on the left */ },
                actions = {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Text
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary
                )
            )
        },
        containerColor = Primary
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            // Search Input Field
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                placeholder = {
                    Text(
                        "What are you searching for?",
                        color = White.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = White
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Text,
                    unfocusedTextColor = Text,
                    focusedContainerColor = Primary.copy(alpha = 0.5f),
                    unfocusedContainerColor = Primary.copy(alpha = 0.5f),
                    cursorColor = Text,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            // Divider
            HorizontalDivider(
                color = White.copy(alpha = 0.3f)
            )

            when {
                searchQuery.isBlank() -> {
                    EmptyState(message = "You have no recent searches")
                }
                searchResults.isEmpty() -> {
                    EmptyState(message = "No products found for \"$searchQuery\"")
                }
                else -> {
                    LazyVerticalGrid (
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp) // Adds space between items
                    ) {
                        items(searchResults) { p ->
                            androidx.compose.material3.Card(
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = White.copy(alpha = 0.08f))
                            ) {
                                androidx.compose.foundation.layout.Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    coil.compose.AsyncImage(
                                        model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                            .data(p.imageUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = p.name,
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = p.name, color = White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    val priceText = "$" + String.format("%.2f", p.price)
                                    Text(text = priceText, color = White, fontSize = 12.sp)
                                }
                            }
                        }
                    }
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        items(searchResults) {product ->
//                            ProductCard(product = product, name = "Search")
//                        }
//                    }
                }
            }

            // Content Area
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // "Recent Searches" Title - this is part of the empty state
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Recent Searches",
                        color = Text,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                        fontSize = 16.sp
                    )

                    // Empty state message in the center
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = White.copy(alpha = 0.7f),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You have no recent searches",
                            color = White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = White.copy(alpha = 0.7f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        }
    }
}