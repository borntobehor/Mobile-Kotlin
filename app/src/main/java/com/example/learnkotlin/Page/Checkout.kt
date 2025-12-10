package com.example.learnkotlin.Page

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learnkotlin.Components.BackAppBar
import com.example.learnkotlin.Components.CartItem
import com.example.learnkotlin.Components.CartViewModel
import com.example.learnkotlin.Components.Product
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text as TextColor

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewOrderScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
) {
    val product by cartViewModel.buyNowProduct.collectAsState()
    val cartItems = cartViewModel.cartItems

    Scaffold(
        topBar = { BackAppBar(navController, "Review", cartViewModel) },
        containerColor = Primary
    ) { paddingValues ->
        val product = product
        if (product == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Nothing to review", color = TextColor, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TextColor,
                        contentColor = Primary
                    )
                ) { Text("Back") }
            }
            return@Scaffold
        }

//        val p: Product = product!!
        val cartItemForThisProduct = cartItems[product.id]
        val quantity = cartItemForThisProduct?.quantity ?: 1
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = TextColor),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = product.image),
                            contentDescription = product.name,
                            modifier = Modifier
                                .clip(shape = CardDefaults.shape)
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        /*
                        Image(
                            painter = painterResource(id = p.image),
                            contentDescription = p.name,
                            modifier = Modifier
                                .clip(shape = CardDefaults.shape)
                                .fillMaxWidth()
                                .height(300.dp)
                            ,
                            contentScale = ContentScale.Crop
                        )
                         */
                        Spacer(modifier = Modifier.height(10.dp))
//                    Text(text = p.name, color = Primary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(
                            text = product.name,
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        /*
                        Text(
                            text = p.description,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                        */
                        Text(
                            text = product.description,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                        HorizontalDivider(
                            modifier = Modifier.clip(CircleShape),
                            thickness = 1.5.dp,
                            Primary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "Amount:",
                                color = Primary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${product.amount} ml",
                                color = Primary,
                                fontWeight = FontWeight.Medium
                            )
                            /*
                            Text(
                                text = "${p.amount} ml",
                                color = Primary,
                                fontWeight = FontWeight.Medium
                            )
                             */
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "Price:", color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = String.format("%.2f $", product.price), color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                            /*
                            Text(
                                text = String.format("%.2f $", p.price)
                                , color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                             */
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = TextColor, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Quantity:",
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                        QuantitySelector(
                            modifier = Modifier.size(100.dp),
                            quantity = quantity,
                            onIncrease = { cartViewModel.increaseQuantity(product) },
                            onDecrease = { cartViewModel.decreaseQuantity(product) },
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Total price:",
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format("$%.2f", product.price * quantity),
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Button(
                onClick = { navController.navigate(Routes.PAYMENT) },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextColor,
                    contentColor = Primary
                ),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text(text = "Proceed to Payment", fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
) {
    Scaffold(
        topBar = { BackAppBar(navController, "Payment", cartViewModel) },
        containerColor = Primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Shipping Location",
                    color = TextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    enabled = false,
                    placeholder = { Text(text = "Enter address (demo)", color = Primary) },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = TextColor,
                        disabledIndicatorColor = Primary,
                        disabledTextColor = Primary,
                        disabledPlaceholderColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Payment Method",
                    color = TextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                PaymentMethods()
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        // In a real app, handle the payment confirmation.
                        cartViewModel.clearBuyNowProduct()
                        navController.navigate(Routes.MAIN) {
                            popUpTo(Routes.MAIN_APP_GRAPH) { inclusive = false }
                        }
                    },
                    modifier = Modifier.weight(2f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TextColor,
                        contentColor = Primary
                    )
                ) { Text("Pay Now", fontSize = 18.sp) }
            }
        }
    }
}

@Composable
private fun PaymentMethods() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        MethodRow(label = "Visa")
        MethodRow(label = "MasterCard")
        MethodRow(label = "Bank Transfer")
    }
}

@Composable
private fun MethodRow(label: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TextColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = Primary, fontWeight = FontWeight.Medium)
            Text(text = "Select", color = Primary)
        }
    }
}