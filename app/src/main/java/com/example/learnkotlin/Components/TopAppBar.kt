package com.example.learnkotlin.Components

import android.R.attr.contentDescription
import android.R.attr.navigationIcon
import android.R.attr.onClick
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.learnkotlin.Page.Routes
import com.example.learnkotlin.R
import com.example.learnkotlin.ui.theme.*
import kotlinx.coroutines.launch


val icon = Primary
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier,
    cartItemCount: Int,
    onNavigationIconClick: () -> Unit,
    navController: NavController,
) {
    TopAppBar (
        title = {
            Text("ScentHub")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Primary,
            titleContentColor = Text
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavigationIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Text
                )
            }
        },
        actions = {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .background(Text)
                    .padding(14.dp,5.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = icon,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .clickable(
                            onClick = {navController.navigate(Routes.SEARCH)}
                        )
                    ,
                )
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    tint = icon,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .clickable(
                        onClick = {}
                    )
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    tint = icon,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .clickable(
                            onClick = {
                                navController.navigate(Routes.FAVORITES)
                            }
                        )
                )
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge() {
                                Text(text = "$cartItemCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = icon,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .clickable(
                                onClick = {
                                    navController.navigate(Routes.CART)
                                }
                            ),
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
        },
    )
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    content: @Composable () -> Unit,
    onLogOutClick: () -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp),
                drawerContainerColor = Text,
                drawerContentColor = Primary,
                drawerShape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column() {
                        Box() {
                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp, top = 50.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.new_1),
                                    contentDescription = "Profile",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                )
                                Spacer(Modifier.width(20.dp))
                                Text(
                                    "Meng Chu",
                                    style = TextStyle(
                                        color = Primary,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = Modifier
//                                        .offset(0.dp, 0.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        HorizontalDivider(
                            modifier = Modifier.clip(CircleShape),
                            thickness = 1.dp,
                            color = Primary
                        )
                        Spacer(Modifier.height(20.dp))
                        Box() {
                            ListOfLink(
                                onNavigate = { route ->
                                    scope.launch {
                                        // Close the drawer first to avoid visual glitches/crashes
                                        drawerState.close()
                                        if (route.isNotEmpty()) {
                                            navController.navigate(route) {
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                },
                                onExitClick = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                onLogOutClick = onLogOutClick
                            )
                        }
                    }
                }
            }
        }
    ) {
        content()
    }
}

@Composable
fun ListOfLink(
    onNavigate: (String) -> Unit,
    onExitClick: () -> Unit,
    onLogOutClick: () -> Unit,
) {
    val lists = listOf<String>(
        "My Account",
        "Security",
        "Payment",
        "Language",
        "Log Out",
        "Exit"
    )
    var selectedItem by remember { mutableStateOf("") }
    LazyColumn(
        Modifier,
    ) {
        items(lists) { list ->
            Text(
                text = list,
                style = TextStyle(
                    color = Primary,
                    fontSize = 16.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    // Clip the area to a shape. This makes the ripple "bounded" to this shape.
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        onClick = {
                            selectedItem = list
                            when (list) {
                                "My Account" -> onNavigate("my_account")
//                                "Security" -> onNavigate("Security")
//                                "Payment" -> onNavigate("Payment")
//                                "Language" -> onNavigate("Language")
                                "Log Out" -> onLogOutClick()
                                "Exit" -> onExitClick()
                            }
                        },
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            )
        }
    }

}