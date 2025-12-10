package com.example.learnkotlin.Page

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.White
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.learnkotlin.ui.theme.Text

import androidx.navigation.compose.NavHost // Add this import
import androidx.navigation.compose.composable // Add this import
import androidx.navigation.navigation
import com.example.learnkotlin.Components.AppBar
import com.example.learnkotlin.Components.CategoryScreen
import com.example.learnkotlin.Components.NavigationDrawer
import com.example.learnkotlin.Components.CartViewModel

@Composable
fun MainScreen(navController: NavController, onLogout: () -> Unit, cartViewModel: CartViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val cartItemCount = cartViewModel.cartItems.size
    val internalNavController = rememberNavController()
    var selectedItem by remember { mutableStateOf("Men Fragrance") }
    NavigationDrawer(
        drawerState = drawerState,
        onLogOutClick = onLogout,
        navController = navController,
        content = {
            Scaffold(
                topBar = {
                    AppBar(modifier = Modifier, cartItemCount, onNavigationIconClick = {
                        scope.launch { drawerState.open() }
                    },
                        navController = navController
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                ,
                containerColor = Primary
            ) { it ->
                Column(
                    modifier = Modifier.padding(top = it.calculateTopPadding())
                ) {
//                    productCatalog.keys.forEach { categoryName ->
//                        val productMap = productCatalog[categoryName]
//                        if (productMap != null) {
//                            CategoryScreen(
//                                categoryName = categoryName,
//                                navController = navController,
//                                cartViewModel = cartViewModel
//                            )
//                        }
//                    }
                    HorizontalDivider(modifier = Modifier, thickness = 1.dp, color = Text)
                    LazyRow() {
                        var listOfType = listOf<String>(
                            "Men Fragrance",
                            "Women Fragrance",
                            "Eau de Toilette (EDT)",
                            "Eau de Parfum (EDP)",
                            "Unisex Fragrance",
                            "Parfum (Perfume)",
                        )
                        items(listOfType) { typePerfume ->
                            TextButton(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp),
                                onClick = {
                                    selectedItem = typePerfume
                                    internalNavController.navigate(typePerfume) {
                                        popUpTo(internalNavController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                },
    //                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
    //                                containerColor = if (selectedItem == typePerfume) White else Primary,
    //                            ),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(
                                    text = typePerfume,
                                    color = if (selectedItem == typePerfume) White else Text
                                )
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier, thickness = 1.dp, color = Text)
                    val listOfType = listOf(
                        "Men Fragrance", "Women Fragrance",
                        "Eau de Toilette (EDT)",
                        "Eau de Parfum (EDP)",
                        "Unisex Fragrance",
                        "Parfum (Perfume)",
                    )
                    NavHost(
                        navController = internalNavController,
                        startDestination = "main_content",
                        modifier = Modifier.weight(1f),
                        enterTransition = {
                            val initialIndex = listOfType.indexOf(initialState.destination.route)
                            val targetIndex = listOfType.indexOf(targetState.destination.route)

                            if (initialIndex < targetIndex) {
                                slideInHorizontally(initialOffsetX = { it }) + fadeIn(initialAlpha = 10f)
                            } else {
                                slideInHorizontally(initialOffsetX = { -it }) + fadeIn(initialAlpha = 10f)
                            }
                        },
                        exitTransition = {
                            val initialIndex = listOfType.indexOf(initialState.destination.route)
                            val targetIndex = listOfType.indexOf(targetState.destination.route)

                            if (initialIndex < targetIndex) {
                                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(targetAlpha = 10f)
                            } else {
                                slideOutHorizontally(targetOffsetX = { it }) + fadeOut(targetAlpha = 10f)
                            }
                        },
                    ) {
                        navigation(startDestination = "Men Fragrance", route = "main_content") {
                            composable("Men Fragrance") {
                                CategoryScreen(
                                    categoryName = "Men Fragrance",
                                    navController = navController,
                                    cartViewModel = cartViewModel
                                )
                            }
                            composable("Women Fragrance") {
                                CategoryScreen(
                                    categoryName = "Women Fragrance",
                                    navController = navController,
                                    cartViewModel = cartViewModel
                                )
                            }
                            composable("Eau de Toilette (EDT)") {
                                CategoryScreen(
                                    categoryName = "Eau de Toilette (EDT)",
                                    navController = navController,
                                    cartViewModel = cartViewModel
                                )
                            }
                            composable("Eau de Parfum (EDP)") {
                                CategoryScreen(
                                    categoryName = "Eau de Toilette (EDT)",
                                    navController = navController,
                                    cartViewModel = cartViewModel
                                )
                            }
                            composable("Unisex Fragrance") {
                                CategoryScreen(
                                    categoryName = "Unisex Fragrance",
                                    navController = navController,
                                    cartViewModel = cartViewModel
                                )
                            }
                            composable("Parfum (Perfume)") {
                            }
                        }
                    }
                }
            }
        },
    )
}



//@Preview
//@Composable
//fun previewWomen(
//) {
//    Box(Modifier.fillMaxSize()) {
//        MainScreen(navController = rememberNavController())
//    }
////    CategoryScreen(categoryName = "Women Fragrance", navController = rememberNavController())
//}
