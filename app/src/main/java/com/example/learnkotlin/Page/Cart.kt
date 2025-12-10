package com.example.learnkotlin.Page

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.learnkotlin.Components.BackAppBar
import com.example.learnkotlin.Components.CartItem
import com.example.learnkotlin.Components.CartViewModel
import com.example.learnkotlin.ui.theme.Primary
import com.example.learnkotlin.ui.theme.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems.values.toList()
    val totalPrice by cartViewModel.totalPrice.collectAsState()
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { BackAppBar(navController, "Cart", cartViewModel) },
        containerColor = Primary,
        bottomBar = {
            CartSummary(totalPrice = totalPrice) {
                navController.navigate(Routes.PAYMENT)
            }
        }
    ) { it ->
        if (cartItems.isEmpty()) {
            EmptyCartState()
        } else {
            LazyColumn(
                modifier = Modifier.padding(top = it.calculateTopPadding(),bottom = it.calculateBottomPadding()),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(cartItems, key = {it.product.id}) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        onIncrease = { cartViewModel.increaseQuantity(cartItem.product) },
                        onDecrease = { cartViewModel.decreaseQuantity(cartItem.product) },
                        onRemove = { cartViewModel.removeFromCart(cartItem.product) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
        ,
        colors = CardDefaults.cardColors(
            containerColor = Text
        ),
    ) {
        Column (
            modifier = Modifier
                .padding(10.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = CardDefaults.shape)
                    ,
                    painter = rememberAsyncImagePainter(cartItem.product.image),
                    contentDescription = cartItem.product.name,
                    contentScale = ContentScale.Crop
                )
                Column (
                    modifier = Modifier
                        .padding(10.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = cartItem.product.name,
                        color = Primary,
                        style = TextStyle(
                            fontSize = 22.sp
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = cartItem.product.description,
                        fontWeight = FontWeight.Medium,
                        color = Primary
                    )
                    Text(
                        text = String.format("Price: $ %.2f", cartItem.product.price),
                        fontWeight = FontWeight.Medium,
                        color = Primary
                    )
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column {
                    Text(
                        text = "Quantity",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    QuantitySelector(
                        quantity = cartItem.quantity,
                        onIncrease = onIncrease,
                        onDecrease = onDecrease,
                    )
                }
                Button (
                    modifier = Modifier
                        .heightIn(min = 33.dp, max = 34.dp)
                        .sizeIn(minWidth = 1.dp, minHeight = 1.dp)
                    ,
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 5.dp),
                    onClick = onRemove,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Text
                    ),

                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_menu_delete),
                        contentDescription = "Remove",
                        tint = Text,
                    )
                    Text(
                        text = "Delete",
                        color = Text
                    )
                }
            }
        }
    }
}

@Composable
fun QuantitySelector(
    modifier: Modifier = Modifier,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Row(
        modifier = Modifier
            .heightIn(min = 33.dp, max = 34.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Button(
            onClick = onDecrease,
            modifier = Modifier
                .sizeIn(minWidth = 1.dp, minHeight = 1.dp)
            ,
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Text,
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Decrease",
                tint = Text,
            )
        }
        Text(
            modifier = Modifier
                .clip(CircleShape)
                .widthIn(min = 80.dp)
                .height(34.dp)
                .background(Primary)
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ,
            text = "$quantity",
            fontWeight = FontWeight.Bold,
            color = Text,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onIncrease,
            modifier = Modifier
                .sizeIn(minWidth = 1.dp, minHeight = 1.dp)
            ,
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Text,
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_input_add),
                contentDescription = "Increase",
                tint = Text,
            )
        }
    }
}

@Composable
fun CartSummary(
    totalPrice: Double,
    onCheckout: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Text),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Text(
                    text = String.format("$ %.2f", totalPrice),
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                ,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Text
                )
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "Checkout",
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Text
                )
            }
        }
    }
}

@Composable
fun EmptyCartState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Your cart is empty.",
            fontWeight = FontWeight.Bold,
            color = Primary
        )
    }
}
