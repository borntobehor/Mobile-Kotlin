package com.example.learnkotlin.Page

import android.app.Application
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.learnkotlin.Components.CartItem
import com.example.learnkotlin.Components.CartViewModel
import com.example.learnkotlin.Login.AuthViewModel
import com.example.learnkotlin.Page.Routes.AUTH_GRAPH
import com.example.learnkotlin.Page.Routes.MAIN
import com.example.learnkotlin.Page.Routes.MAIN_APP_GRAPH
import com.example.learnkotlin.Setting.MyAccountScreen
import com.example.learnkotlin.ForgetPassword
import com.example.learnkotlin.LoginScreen
import com.example.learnkotlin.OtpScreen
import com.example.learnkotlin.SignUp

//@Preview
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()


    val application = LocalContext.current.applicationContext as Application
    // Provide a single CartViewModel instance tied to the NavHost scope so it persists and can access Application
    val cartViewModel: CartViewModel = androidx.lifecycle.viewmodel.compose.viewModel<CartViewModel>(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application))
    val onLogout: () -> Unit = {
        authViewModel.logout()
        navController.navigate(AUTH_GRAPH) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) MAIN_APP_GRAPH else AUTH_GRAPH
    ) {

        authGraph(navController, authViewModel)
        mainAppGraph(navController, onLogout, cartViewModel)
    }
}

fun NavGraphBuilder.authGraph(navController: NavController, authViewModel: AuthViewModel) {
    navigation(startDestination = Routes.LOGIN, route = Routes.AUTH_GRAPH) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.MAIN_APP_GRAPH) {
                        popUpTo(Routes.AUTH_GRAPH) {
                            inclusive = true
                        }
                    }
                },
                onForgotPasswordClicked = { navController.navigate(Routes.FORGET_PASSWORD) },
                onSignUpClicked = { navController.navigate(Routes.SIGNUP) },
                authViewModel = authViewModel
            )
        }
        composable(Routes.SIGNUP) {
            SignUp (
                onSignUpSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable(Routes.FORGET_PASSWORD) {
            ForgetPassword(
                onContinueClicked = { navController.navigate(Routes.OTP) },
                onBackClicked = { navController.popBackStack() },
            )
        }
        composable(Routes.OTP) {
            OtpScreen(
                onContinueClicked = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onBackClicked = { navController.popBackStack() },
            )
        }
    }
}

fun NavGraphBuilder.mainAppGraph(navController: NavController, onLogout: () -> Unit, cartViewModel: CartViewModel) {
    navigation(startDestination = Routes.MAIN, route = MAIN_APP_GRAPH) {
        composable(Routes.MAIN) {
            MainScreen(navController = navController, onLogout = onLogout, cartViewModel = cartViewModel)
        }
        composable(Routes.ALL_PRODUCTS) {
            AllProduct(navController = navController)
        }
        composable(Routes.FAVORITES) {
            FavoritePage(navController = navController)
        }
        composable(Routes.MY_ACCOUNT) {
            MyAccountScreen(navController = navController) {
                navController.popBackStack()
            }
        }
        composable(Routes.SEARCH) {
            SearchScreen(onCloseClicked = { navController.popBackStack() })
        }
        composable(Routes.CART) {
            Cart(navController = navController, cartViewModel = cartViewModel)
        }
        composable(Routes.REVIEW_ORDER) {
            ReviewOrderScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable(Routes.PAYMENT) {
            PaymentScreen(navController = navController, cartViewModel = cartViewModel)
        }
    }
}