package com.example.learnkotlin.Page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learnkotlin.ui.theme.Text
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.Alignment
import com.example.learnkotlin.Components.FavoriteViewModel
import com.example.learnkotlin.Components.Product
import com.example.learnkotlin.Components.CartViewModel
import com.example.learnkotlin.ui.theme.Primary
import kotlin.collections.takeLast


val normal = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = Primary
)

val title = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    color = Primary,
)

val head = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    color = Text,
)
@Composable
fun Products(
    productList: List<Product> = listOf(),
    typeOfProduct: String,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .background(Color.Red)
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = typeOfProduct,
                color = Text,
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .background(Text)
                    .padding(10.dp, 2.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { navController.navigate("all_products") },
                    ),
                text = "View all",
                color = Primary,
                fontWeight = FontWeight.Medium,
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
//                .weight(1f)
            ,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            productList.takeLast(2).reversed().forEach { product ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 100.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Text, shape = CardDefaults.shape)
                            .shadow(
                                elevation = 0.dp,
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Text,
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
//                                .padding(10.dp),
//                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Image(
                                painter = painterResource(id = product.image),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(shape = CardDefaults.shape),
                            )
                            Spacer(Modifier.height(2.dp))
                            Column (
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(10.dp)
                                ,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column() {
                                    Text(
                                        text = product.name,
                                        style = title,
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    HorizontalDivider(thickness = 1.dp, color = Primary)
                                    Spacer(Modifier.height(10.dp))
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    if (product.description != "") {
                                        Text(
                                            text = "${product.description}",
                                            style = normal
                                        )
                                    } else {
                                        Text(
                                            text = "No Description",
                                            style = normal
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Text(
                                            text = "${product.amount} ml",
                                            style = normal,
                                        )
                                        VerticalDivider(thickness = 1.dp, color = Text)
                                        Text(
                                            text = String.format("$ %.2f", product.price),
                                            style = normal,
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp, bottom= 10.dp)
                                ,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                val isFavorite = FavoriteViewModel.isFavorite(product)
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = Primary,
                                    modifier = Modifier
                                        .padding()
                                        .clip(RoundedCornerShape(50))
                                        .clickable(
                                            onClick = {
                                                FavoriteViewModel.toggleFavorite(product)
                                            }
                                        )
                                        .padding(5.dp)
                                    ,
                                )
                                Button(
                                    modifier = Modifier
                                        .defaultMinSize(1.dp, 1.dp)
                                    ,
                                    contentPadding = PaddingValues(20.dp, 8.dp),
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = Primary,
                                        contentColor = Text
                                    ),
                                    onClick = {
                                        cartViewModel.addToCart(product)
                                    },
                                ) {
                                    Text(
                                        text = "Add to cart",
                                        style = normal,
                                        color = Text
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, name: String) {
    Card(
        modifier = Modifier
//            .fillMaxWidth()
            .width(175.dp)
            .heightIn(min = 200.dp, max = 500.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = Text
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Text
        )
    ) {
        Box(
            modifier = Modifier
                .height(150.dp),
        ) {
            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (product.name != "") {
                Text(
                    text = product.name,
                    style = title
                )
            }
             else {
                Text(
                    text = "No brand name",
                    style = title
                )
             }
            HorizontalDivider(thickness = 1.dp, color = Primary)
            if (product.description != "") {
                Text(
                    text = "${product.description}",
                    style = normal
                )
            } else {
                Text(
                    text = "No Description",
                    style = normal
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${product.amount} ml",
                    style = normal
                )
                Text(
                    text = "$ ${product.price}",
                    style = normal
                )
            }
            if (name == "Favorites") {
                Button (
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Primary,
                        contentColor = Text
                    ),
                    onClick = {
                        FavoriteViewModel.toggleFavorite(product)
                    }
                ) {
                    Text(text = "Remove Favorite")
                }
            } else if(name == "All Products" || name == "Search") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(start = 10.dp, end = 10.dp, bottom= 10.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    val isFavorite = FavoriteViewModel.isFavorite(product)
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Primary,
                        modifier = Modifier
                            .padding()
                            .clip(RoundedCornerShape(50))
                            .clickable(
                                onClick = {
                                    FavoriteViewModel.toggleFavorite(product)
                                }
                            )
                            .padding(5.dp)
                        ,
                    )
                    Button(
                        modifier = Modifier
                            .defaultMinSize(1.dp, 1.dp)
                        ,
                        contentPadding = PaddingValues(20.dp, 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Primary,
                            contentColor = Text
                        ),
                        onClick = {},
                    ) {
                        Text(
                            text = "Add to cart",
                            style = normal,
                            color = Text
                        )
                    }
                }
            }
        }
    }
}
