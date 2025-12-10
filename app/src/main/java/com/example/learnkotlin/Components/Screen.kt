package com.example.learnkotlin.Components

sealed class Screen(val screen: String) {
    data object HomeScreen: Screen("Home Screen")
}