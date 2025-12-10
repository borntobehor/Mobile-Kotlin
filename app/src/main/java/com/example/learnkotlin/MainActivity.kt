package com.example.learnkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.learnkotlin.Page.AppNavigation
import com.example.learnkotlin.ui.theme.LearnKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            LearnKotlinTheme {
//                val navController = rememberNavController()
//                MainScreen(navController = navController)
                AppNavigation()
            }
        }
    }
}