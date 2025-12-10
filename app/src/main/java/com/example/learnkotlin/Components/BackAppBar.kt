package com.example.learnkotlin.Components

import android.R
import android.R.attr.contentDescription
import android.R.attr.navigationIcon
import android.R.attr.onClick
import android.app.Application
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.learnkotlin.Page.Routes
import com.example.learnkotlin.Page.head
import com.example.learnkotlin.Page.normal
import com.example.learnkotlin.Page.title
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text
import com.example.learnkotlin.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackAppBar(
    navController: NavController,
    name: String,
    cartViewModel: CartViewModel? = null,
) {
    val favoriteProducts = FavoriteViewModel.favoriteProductId.value
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
        ,
        title = { Text(text = name, style = head) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Text
                )
            }
        },
        actions = {
            if (!favoriteProducts.isEmpty() && name == "Favorites") {
                Button(
                    modifier = Modifier
                        .defaultMinSize(1.dp, 1.dp)
                        .padding(end = 16.dp)
                        .shadow(
                            elevation = 0.dp,
                        ),
                    contentPadding = PaddingValues(20.dp, 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Text,
                    ),
                    onClick = {
                        FavoriteViewModel.clearAllFavorites()
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_menu_delete),
                        contentDescription = "Delete",
                        tint = Primary,
                    )
                    Text(
                        text = "Clear All Favorite",
                        style = title,
                        color = Primary
                    )
                }
            } else if (name == "All Products") {
                Button(
                    modifier = Modifier
                        .defaultMinSize(1.dp, 1.dp)
                        .padding(end = 16.dp)
                        .shadow(
                            elevation = 0.dp,
                        ),
                    contentPadding = PaddingValues(20.dp, 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Text,
                    ),
                    onClick = {
                        navController.navigate(Routes.SEARCH)
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_menu_search),
                        contentDescription = "Search",
                        tint = Primary,
                    )
                    Text(
                        text = "Search",
                        style = title,
                        color = Primary
                    )
                }
            } else if (name == "Cart" && (cartViewModel?.cartItems?.isNotEmpty() == true)) {
                Button(
                    modifier = Modifier
                        .defaultMinSize(1.dp, 1.dp)
                        .padding(end = 16.dp)
                        .shadow(
                            elevation = 0.dp,
                        ),
                    contentPadding = PaddingValues(20.dp, 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Text,
                    ),
                    onClick = {
                        cartViewModel?.clearCart()
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_menu_delete),
                        contentDescription = "Delete Cart All",
                        tint = Primary,
                    )
                    Text(
                        text = "Delete All",
                        style = title,
                        color = Primary
                    )
                }
            }
        }
    )
}